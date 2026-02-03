package com.gr.manchid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gr.manchid.ui.HomeFragment
import com.gr.manchid.ui.post.PostFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //home fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()

        // post fragment


    }
}