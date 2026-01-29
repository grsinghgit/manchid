package com.gr.manchid.model

data class OrganizerStatus(
    val dashboardInitialized: Boolean = false,
    val schemaVersion: Int = 1,
    val isActive: Boolean = true,
    val isVerified: Boolean = false,
    val isBlocked: Boolean = false
)
