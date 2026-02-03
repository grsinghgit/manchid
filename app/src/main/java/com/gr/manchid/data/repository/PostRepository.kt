package com.gr.manchid.data.repository
import com.gr.manchid.models.Post

class PostRepository {
    fun getPosts() : List<Post> {
        return listOf(
            Post(1, "namaste", "my munch welcone", "sahib"),
                    Post(2, "Update", "App development is fun hai", "Team")
        )
    }

}