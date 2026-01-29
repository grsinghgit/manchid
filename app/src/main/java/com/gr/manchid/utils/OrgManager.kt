package com.gr.manchid.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.gr.manchid.model.OrgData
import com.gr.manchid.model.OrganizerStats
import com.gr.manchid.model.OrganizerStatus
import com.gr.manchid.model.YouTubeLink

object OrgManager {

    private val db = FirebaseFirestore.getInstance()

    // ================================
    // CHECK: organizer already exists?
    // ================================
    fun checkOrganizer(
        googleUid: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        db.collection("organizers")
            .document(googleUid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val mymanchId = doc.getString("mymanchId")
                    onResult(true, mymanchId)
                } else {
                    onResult(false, null)
                }
            }
            .addOnFailureListener {
                onResult(false, null)
            }
    }

    // ================================
    // REGISTER ORGANIZER
    // ================================
    fun registerOrganizer(
        googleUid: String,
        data: Map<String, Any>,
        onSuccess: (String) -> Unit,
        onError: () -> Unit
    ) {
        val mymanchId = (10000000..99999999).random().toString()

        val organizerRef = db.collection("organizers").document(googleUid)
        val mymanchRef = db.collection("mymanch").document(mymanchId)

        // ensure MyManch ID unique
        mymanchRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                // retry if collision
                registerOrganizer(googleUid, data, onSuccess, onError)
            } else {

                val batch = db.batch()

                // private organizer data
                batch.set(
                    organizerRef,
                    data + mapOf(
                        "mymanchId" to mymanchId,
                        "createdAt" to System.currentTimeMillis()
                    )
                )

                // public lookup
                batch.set(
                    mymanchRef,
                    mapOf(
                        "organizerUid" to googleUid
                    )
                )

                batch.commit()
                    .addOnSuccessListener { onSuccess(mymanchId) }
                    .addOnFailureListener { onError() }
            }
        }
    }

    // ================================
    // UPDATE ORGANIZER
    // ================================
    fun updateOrganizer(
        googleUid: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("organizers")
            .document(googleUid)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }

    // ================================
    // DELETE ORGANIZER
    // ================================
    // ===============================
    // DELETE ORGANIZER
    // ===============================
    fun deleteOrganizer(
        uid: String,
        mymanchId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val orgRef = db.collection("organizers").document(uid)
        val mymanchRef = db.collection("mymanch").document(mymanchId)

        val batch = db.batch()

        // 🔹 delete meta docs explicitly
        batch.delete(
            orgRef.collection("meta").document("status")
        )
        batch.delete(
            orgRef.collection("meta").document("stats")
        )
        batch.delete(
            orgRef.collection("meta").document("orgData")
        )

        // 🔹 delete main organizer doc
        batch.delete(orgRef)

        // 🔹 delete public lookup
        batch.delete(mymanchRef)

        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onFailure(it.message ?: "Delete failed")
            }
    }


    //check initializedashboard



    fun initializeDashboard(
        uid: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val statusRef = db.collection("organizers")
            .document(uid)
            .collection("meta")
            .document("status")

        val statsRef = db.collection("organizers")
            .document(uid)
            .collection("meta")
            .document("stats")



        //val youtubeRef = db.collection("organizers")
           // .document(uid)
            //.collection("meta")
           // .document("ytlinks")

        // 🔹 Step 1: check already initialized or not
        statusRef.get()
            .addOnSuccessListener { doc ->

                // ✅ dashboard already initialized → DO NOTHING
                if (doc.exists() && doc.getBoolean("dashboardInitialized") == true) {
                    onSuccess()
                    return@addOnSuccessListener
                }

                // 🔹 Step 2: create default objects from data classes
                val status = OrganizerStatus(
                    dashboardInitialized = true,
                    schemaVersion = 1
                )

                val stats = OrganizerStats()
                //val ytlinks = YouTubeLink()


                val batch = db.batch()

                // 🔹 Step 3: merge = safe (no overwrite of existing data)
                batch.set(statusRef, status, SetOptions.merge())
                batch.set(statsRef, stats, SetOptions.merge())
               // batch.set(youtubeRef, ytlinks, SetOptions.merge())

                // 🔹 Step 4: commit
                batch.commit()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure(it.message ?: "Dashboard initialization failed")
                    }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to check dashboard state")
            }
    }

    //check initialize dsbord




        //youtube link add

        fun addYouTubeLink(
            uid: String,
            link: YouTubeLink,
            onSuccess: () -> Unit,
            onFailure: (String) -> Unit
        ) {

                db.collection("organizers")
                .document(uid)
                .collection("youtube")   // ✅ collection (not document)
                .add(link)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "Failed to add YouTube link")
                }
        }




        //get all link in recycler vie
        fun getYouTubeLinks(
            uid: String,
            onSuccess: (List<YouTubeLink>) -> Unit,
            onFailure: (String) -> Unit
        ) {
            db.collection("organizers")
                .document(uid)
                .collection("youtube")
                .orderBy("addedAt")
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = snapshot.toObjects(YouTubeLink::class.java)
                    onSuccess(list)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "Failed to load YouTube links")
                }
        }

        //delete youtube link

        fun deleteYouTubeLink(
            uid: String,
            youtubeDocId: String,
            onSuccess: () -> Unit,
            onFailure: (String) -> Unit
        ) {
            db.collection("organizers")
                .document(uid)
                .collection("youtube")
                .document(youtubeDocId)
                .delete()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "Failed to delete YouTube link")
                }
        }





        //createonlyfunctionforadddataclass

    fun createOrgDataIfNotExists(
        uid: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val docRef = db.collection("organizers")
            .document(uid)
            .collection("meta")
            .document("orgData")

        docRef.get()
            .addOnSuccessListener { doc ->

                // ✅ already exists → DO NOTHING
                if (doc.exists()) {
                    onSuccess()
                    return@addOnSuccessListener
                }

                //  does not exist → CREATE ONCE
                val orgData = OrgData()

                docRef.set(orgData)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure(it.message ?: "Failed to create orgData")
                    }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to check orgData")
            }
    }









    //last
}
