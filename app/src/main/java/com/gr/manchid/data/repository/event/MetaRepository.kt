package com.gr.manchid.data.repository.event

import com.gr.manchid.models.eventm.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.gr.manchid.data.repository.event.MetaEvent



class MetaRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getMetaData(
        uid: String,
        callback: (Resource<List<MetaEvent>>) -> Unit
    ){

        callback(Resource.Loading())

        db.collection("organizers")
            .document(uid)
            .collection("meta")
            .addSnapshotListener { snapshot, error ->

                if(error != null){
                    callback(Resource.Error(error.message ?: "Error"))
                    return@addSnapshotListener
                }

                val list = mutableListOf<MetaEvent>()

                snapshot?.documents?.forEach {

                    val data = it.toObject(MetaEvent::class.java)

                    data?.let { ev ->
                        list.add(ev.copy(id = it.id))
                    }
                }

                callback(Resource.Success(list))
            }
    }





    fun addMetaData(
        uid: String,
        event: MetaEvent
    ){

        db.collection("organizers")
            .document(uid)
            .collection("meta")
            .add(event)

    }


    fun deleteMetaData(
        uid: String,
        docId: String
    ){

        db.collection("organizers")
            .document(uid)
            .collection("meta")
            .document(docId)
            .delete()

    }


    fun updateMeta(uid:String, event:MetaEvent){

        db.collection("organizers")
            .document(uid)
            .collection("meta")
            .document(event.id)
            .set(event)
    }








}
