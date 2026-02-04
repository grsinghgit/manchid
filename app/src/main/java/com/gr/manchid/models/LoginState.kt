package com.gr.manchid.models

import com.google.firebase.auth.FirebaseUser

sealed class LoginState {
    object Idle : LoginState()

    object Loading : LoginState()

    data class LoggedIn(
        val user: FirebaseUser?
    ) : LoginState()

    object LoggedOut : LoginState()

    data class Error(
        val message: String?
    ) : LoginState()
}
