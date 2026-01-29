package com.gr.manchid.model

data class YouTubeLink(
    val url: String = "",
    val title: String = "",
    val thumbnail: String = "",
    val isActive: Boolean = true,
    val addedAt: Long = System.currentTimeMillis()
)

