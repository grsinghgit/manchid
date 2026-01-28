package com.gr.manchid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gr.manchid.utils.MyVar

class MainActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Data class object receive
        val myVar = intent.getParcelableExtra<MyVar>("MY_DATA")

// yturl ko ek normal String variable me daal do
        val ytUrl: String = myVar?.yturl ?: ""

        Toast.makeText(this, "URL = $ytUrl", Toast.LENGTH_LONG).show()










        //oncreate end bracket
    }

    //class end
}