package com.gr.manchid.utils

import com.google.firebase.firestore.FirebaseFirestore

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
    fun deleteOrganizer(
        googleUid: String,
        mymanchId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val batch = db.batch()

        batch.delete(db.collection("organizers").document(googleUid))
        batch.delete(db.collection("mymanch").document(mymanchId))

        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }
}
