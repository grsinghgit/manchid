package com.gr.manchid

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity4 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var registerAsOrgBtn: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        registerAsOrgBtn = findViewById(R.id.registerashost)
        registerAsOrgBtn.visibility = View.GONE

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val googleUid = currentUser.uid

        // 🔍 CHECK: Is this UID already an organizer?
        db.collection("organizers")
            .document(googleUid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    // Organizer already registered
                    Toast.makeText(
                        this,
                        "Already registered as organizer",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Not organizer → show button
                    registerAsOrgBtn.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error checking organizer", Toast.LENGTH_SHORT).show()
            }

        // 🔘 Register button click
        registerAsOrgBtn.setOnClickListener {
            registerOrganizer(googleUid)
        }
    }

    // ================================
    // ORGANIZER REGISTRATION LOGIC
    // ================================
    private fun registerOrganizer(googleUid: String) {

        val mymanchId = (10000000..99999999).random().toString()

        val organizerRef = db.collection("organizers").document(googleUid)
        val mymanchRef = db.collection("mymanch").document(mymanchId)

        // Check MyManch ID uniqueness
        mymanchRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                // Retry if ID already used
                registerOrganizer(googleUid)
            } else {

                val batch = db.batch()

                // 1️⃣ Organizer private profile
                batch.set(
                    organizerRef,
                    mapOf(
                        "createdAt" to System.currentTimeMillis()
                    )
                )

                // 2️⃣ Public MyManch lookup
                batch.set(
                    mymanchRef,
                    mapOf(
                        "organizerUid" to googleUid
                    )
                )

                batch.commit().addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Organizer registered successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    registerAsOrgBtn.visibility = View.GONE
                }
            }
        }
    }
}
