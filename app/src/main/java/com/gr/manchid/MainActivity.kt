package com.gr.manchid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gr.manchid.utils.AuthManager
import com.gr.manchid.utils.CheckUserManager

class MainActivity : AppCompatActivity() {
    lateinit var manageeventbtn: Button
    lateinit var registerbtn: Button
    lateinit var signOutBtn: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //AuthManager object initiation which have firebase and gsign..
        AuthManager.init(this)




       // second activity
        manageeventbtn = findViewById<Button>(R.id.btnManageEvents)

        manageeventbtn.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        // third activity

        registerbtn = findViewById<Button>(R.id.btnRegisterHost)

        registerbtn.setOnClickListener {
            AuthManager.signIn(this)
        }

        // signout btn
        signOutBtn = findViewById<Button>(R.id.btnSignOut)
        signOutBtn.setOnClickListener {
            AuthManager.logout()
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
            checkUser()   // 🔄 UI refresh
        }


        // checkuser private function call which is defined in last has all logic visibility
        checkUser()

        // second activity
        val button2 = findViewById<Button>(R.id.btnSubmitEventId)

        button2.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        //
        //








    }
    // private fun space below
    // override function when return to activity
    override fun onResume() {
        super.onResume()
        checkUser()   // 👈 jab activity wapas aaye
    }

    //1 more override function
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        AuthManager.handleResult(
            this,
            requestCode,
            data,
            onSuccess = { user ->
                // ✅ Login success
                Toast.makeText(
                    this,
                    "Welcome ${user?.displayName ?: "User"}",
                    Toast.LENGTH_SHORT
                ).show()

                checkUser()   // 🔥 UI update
            },
            onFailure = { error ->
                // ❌ Login failed / cancelled
                Toast.makeText(
                    this,
                    error.ifEmpty { "Login cancelled" },
                    Toast.LENGTH_SHORT
                ).show()

                checkUser()   // user null hi rahega
            }
        )
    }


    //private function checkuser defined
        private fun checkUser() {
            val user = AuthManager.getCurrentUser()
            val isLoggedIn = CheckUserManager.getUserState(user)

            if (isLoggedIn) {
                manageeventbtn.visibility = View.VISIBLE
                signOutBtn.visibility = View.VISIBLE
                registerbtn.visibility = View.GONE
            } else {
                manageeventbtn.visibility = View.GONE
                registerbtn.visibility = View.VISIBLE
                signOutBtn.visibility = View.GONE
            }
        }



  //last end bracket of class
}