package com.gr.manchid.data.repository.event

import com.google.firebase.firestore.FirebaseFirestore
import com.gr.manchid.data.repository.event.EventData
import com.gr.manchid.models.eventm.Resource



class EventRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getEvents(callback: (Resource<List<EventData>>) -> Unit){

        callback(Resource.Loading())

        db.collection("events")
            .get()
            .addOnSuccessListener { result ->

                val list = result.toObjects(EventData::class.java)
                callback(Resource.Success(list))

            }
            .addOnFailureListener {
                callback(Resource.Error(it.message ?: "Error"))
            }
    }

    fun addEvent(event: EventData){
        db.collection("events")
            .document()
            .set(event)
    }
}
