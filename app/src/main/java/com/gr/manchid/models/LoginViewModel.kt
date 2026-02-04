package com.gr.manchid.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gr.manchid.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    // =========================
    // STATE
    // =========================
    private val _state = MutableLiveData<LoginState>(LoginState.Idle)
    val state: LiveData<LoginState> = _state


    // =========================
    // CHECK CURRENT USER
    // =========================
    fun checkCurrentUser() {

        val user = repo.getCurrentUser()

        if (user != null) {
            _state.value = LoginState.LoggedIn(user)
        } else {
            _state.value = LoginState.LoggedOut
        }
    }


    // =========================
    // GOOGLE FIREBASE LOGIN
    // =========================
    fun firebaseAuthWithGoogle(idToken: String) {

        viewModelScope.launch {

            _state.value = LoginState.Loading

            val result = repo.firebaseSignInWithGoogle(idToken)

            result.onSuccess { user ->
                _state.value = LoginState.LoggedIn(user)
            }

            result.onFailure { error ->
                _state.value = LoginState.Error(
                    error.message ?: "Google Login Failed"
                )
            }
        }
    }


    // =========================
    // SIGN OUT
    // =========================
    fun signOut() {

        viewModelScope.launch {

            repo.signOut()
            _state.value = LoginState.LoggedOut
        }
    }
}
