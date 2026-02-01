package com.gr.manchid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import utube
import com.google.android.material.textfield.TextInputEditText


class MainActivity2 : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit  var yttext: TextView
    private lateinit var editurl: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        yttext = findViewById(R.id.yttext)
         editurl = findViewById(R.id.editurl)
        val btn: Button = findViewById(R.id.submiturl)


        val user = auth.currentUser ?: return

        fetchAllOrganizerData(user.uid){urld->





            //youtube player


            utube.setupPlayer(lifecycle, findViewById(R.id.youtube_player_view), urld)

            yttext.text = urld }


        //send data to firestore
        btn.setOnClickListener {
            val eturl = editurl.text.toString().trim()
            seturl(user.uid,eturl)
        }





    }

    // ================================
    // FETCH ALL ORGANIZER DATA
    // ================================
    private fun fetchAllOrganizerData(uid: String, callback:(String)->Unit) {




                        // 3️⃣ STATS
                        db.collection("organizers")
                            .document(uid)
                            .collection("meta")
                            .document("orgData")
                            .get()
                            .addOnSuccessListener { orgData ->
                             val  url = orgData.getString("field1") ?: "nodata"

                               val urld = extractYoutubeVideoId(url)
                                callback(urld.toString())


                            }

        //submit button




    }

    // url extractor

    private fun extractYoutubeVideoId(url: String): String? {

        val regex = Regex(
            "(?:youtu\\.be/|youtube\\.com\\/(?:watch\\?v=|embed/|v/|shorts/|live/))([A-Za-z0-9_-]{11})"
        )

        val match = regex.find(url)
        return match?.groupValues?.get(1)
    }


    // set value .in Db orgData url
    private fun seturl(uid:String,eturl:String) {
    db.collection("organizers")
    .document(uid)
    .collection("meta")
    .document("orgData")
        .update("field1",eturl )
        .addOnSuccessListener { Toast.makeText(this, "datasuccesfully updated", Toast.LENGTH_SHORT).show() }

    }





//last
}
