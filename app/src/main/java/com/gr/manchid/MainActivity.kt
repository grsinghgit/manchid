package com.gr.manchid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       // second activity
        val button = findViewById<Button>(R.id.btnManageEvents)

        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        // third activity

        // second activity
        val button2 = findViewById<Button>(R.id.btnSubmitEventId)

        button2.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        //
        // 🔥 Firebase init check
        val app = FirebaseApp.initializeApp(this)

        if (app != null) {
            Log.d("FIREBASE_TEST", "✅ Firebase initialized successfully")
        } else {
            Log.e("FIREBASE_TEST", "❌ Firebase NOT initialized")
        }

        // Analytics test
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("firebase_test_event", null)
        Log.d("FIREBASE_TEST", "📊 Analytics event sent")






    }
}