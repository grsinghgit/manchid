package com.gr.manchid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)


        // Add click listeners to cards
        val addProjectCard = findViewById<MaterialCardView>(R.id.addProjectCard)
        val analyticsCard = findViewById<MaterialCardView>(R.id.analyticsCard)

        addProjectCard.setOnClickListener {
            Toast.makeText(this, "Add Project Clicked", Toast.LENGTH_SHORT).show()
            // Add animation
            addProjectCard.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                addProjectCard.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
        }

        analyticsCard.setOnClickListener {
            Toast.makeText(this, "Analytics Clicked", Toast.LENGTH_SHORT).show()
            analyticsCard.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                analyticsCard.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
        }



      //end comlulsury bracket
    }
}