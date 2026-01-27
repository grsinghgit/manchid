package com.gr.manchid.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*

object AuthManager {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    const val RC_SIGN_IN = 101

    // 🔹 Init (call in MainActivity onCreate)
    fun init(activity: Activity) {
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(
                activity.resources.getIdentifier(
                    "default_web_client_id",
                    "string",
                    activity.packageName
                )
            ))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    // 🔹 Start Google Login
    fun signIn(activity: Activity) {
        val intent = googleSignInClient.signInIntent
        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    // 🔹 Handle Result
    fun handleResult(
        activity: Activity,
        requestCode: Int,
        data: Intent?,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (requestCode != RC_SIGN_IN) return

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!, onSuccess, onFailure)
        } catch (e: ApiException) {
            if (e.statusCode == 12501) {
                // 👈 USER CANCELLED → no toast needed
                onFailure("")   // empty message
            }
           else {
                onFailure("")
                onFailure(e.localizedMessage ?: "Google Sign-In Failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception?.localizedMessage ?: "Firebase Auth Failed")
                }
            }
    }

    // 🔹 Logout
    fun logout() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
