package com.gr.manchid.utils



import com.google.firebase.auth.FirebaseUser

object CheckUserManager {

    fun getUserState(user: FirebaseUser?): Boolean {
        // true = logged in
        // false = not logged in
        return user != null
    }
}
