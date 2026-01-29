package com.gr.manchid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {

    private lateinit var tvData: TextView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        tvData = findViewById(R.id.tvOrganizerData)

        val user = auth.currentUser ?: return

        fetchAllOrganizerData(user.uid)
    }

    // ================================
    // FETCH ALL ORGANIZER DATA
    // ================================
    private fun fetchAllOrganizerData(uid: String) {

        val output = StringBuilder()

        // 1️⃣ PROFILE
        db.collection("organizers")
            .document(uid)
            .get()
            .addOnSuccessListener { profile ->

                if (!profile.exists()) {
                    tvData.text = "Organizer profile not found"
                    return@addOnSuccessListener
                }

                output.append("=== ORGANIZER PROFILE ===\n")
                output.append("MyManch ID: ${profile.getString("mymanchId")}\n")
                output.append("Institution: ${profile.getString("institutionName")}\n")
                output.append("Organizer: ${profile.getString("organizerName")}\n")
                output.append("Contact: ${profile.getString("contact")}\n")
                output.append("Category: ${profile.getString("category")}\n\n")

                // 2️⃣ STATUS
                db.collection("organizers")
                    .document(uid)
                    .collection("meta")
                    .document("status")
                    .get()
                    .addOnSuccessListener { status ->

                        output.append("=== STATUS ===\n")
                        output.append("Active: ${status.getBoolean("isActive")}\n")
                        output.append("Verified: ${status.getBoolean("isVerified")}\n")
                        output.append("Blocked: ${status.getBoolean("isBlocked")}\n\n")

                        // 3️⃣ STATS
                        db.collection("organizers")
                            .document(uid)
                            .collection("meta")
                            .document("stats")
                            .get()
                            .addOnSuccessListener { stats ->

                                output.append("=== STATS ===\n")
                                output.append("Views: ${stats.getLong("views")}\n")
                                output.append("Followers: ${stats.getLong("followers")}\n")
                                output.append("Events Posted: ${stats.getLong("eventsPosted")}\n")

                                tvData.text = output.toString()
                            }
                    }
            }
    }
}
