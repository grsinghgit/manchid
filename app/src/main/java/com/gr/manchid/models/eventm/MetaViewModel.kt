package com.gr.manchid.models.eventm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gr.manchid.data.repository.event.MetaEvent
import com.gr.manchid.data.repository.event.MetaRepository

class MetaViewModel : ViewModel(){

    private val repo = MetaRepository()

    private val _metaList = MutableLiveData<Resource<List<MetaEvent>>>()
    val metaList : LiveData<Resource<List<MetaEvent>>> = _metaList

    fun loadMeta(uid:String){
        repo.getMetaData(uid){
            _metaList.postValue(it)
        }
    }

    fun addMeta(uid:String, event:MetaEvent){
        repo.addMetaData(uid, event)
    }

    fun deleteMeta(uid:String, id:String){
        repo.deleteMetaData(uid, id)
    }
    fun updateMeta(uid:String, event:MetaEvent){
        repo.updateMeta(uid,event)
    }


}
