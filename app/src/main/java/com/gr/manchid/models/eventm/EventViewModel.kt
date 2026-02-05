package com.gr.manchid.models.eventm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.gr.manchid.data.repository.event.EventData
import com.gr.manchid.data.repository.event.EventRepository



class EventViewModel : ViewModel() {

    private val repo = EventRepository()

    private val _events = MutableLiveData<Resource<List<EventData>>>()
    val events : LiveData<Resource<List<EventData>>> = _events

    fun loadEvents(){
        repo.getEvents {
            _events.postValue(it)
        }
    }

    fun addEvent(event: EventData){
        repo.addEvent(event)
    }
}
