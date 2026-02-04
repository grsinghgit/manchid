package com.gr.manchid.data.repository

import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.ClearCredentialStateRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager
) {

    // =========================
    // Firebase Google Login
    // =========================
    suspend fun firebaseSignInWithGoogle(
        idToken: String
    ): Result<FirebaseUser?> {

        return try {

            Log.d("AUTH_REPO", "Firebase Google Login Start")

            val credential = GoogleAuthProvider.getCredential(idToken, null)

            val result = firebaseAuth
                .signInWithCredential(credential)
                .await()

            Log.d("AUTH_REPO", "Firebase Google Login Success")

            Result.success(result.user)

        } catch (e: Exception) {

            Log.e("AUTH_REPO", "Firebase Login Error ${e.message}")

            Result.failure(e)
        }
    }

    // =========================
    // Current User
    // =========================
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // =========================
    // Sign Out
    // =========================
    suspend fun signOut(): Result<Unit> {
        return try {

            firebaseAuth.signOut()

            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
