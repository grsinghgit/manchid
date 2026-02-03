package com.gr.manchid.ui.post
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gr.manchid.data.repository.PostRepository
import com.gr.manchid.models.Post


class PostViewModel : ViewModel() {
    private val repository = PostRepository()
    val postsLiveData = MutableLiveData<List<Post>>()

    fun loadPost() {
        postsLiveData.value = repository.getPosts()

    }


}