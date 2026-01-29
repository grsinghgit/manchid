package com.gr.manchid.model

data class OrganizerProfile(

    // basic identity
    val institutionName: String = "",
    val organizerName: String = "",
    val contact: String = "",

    // locked after registration
    val category: String = "",

    // system generated
    val mymanchId: String = "",

    // timestamps
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
