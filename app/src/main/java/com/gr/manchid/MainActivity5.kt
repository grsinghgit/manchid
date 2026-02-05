package com.gr.manchid

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gr.manchid.ui.ArtistFragment
import com.gr.manchid.ui.EventFragment
import com.gr.manchid.ui.TodayFragment
import com.gr.manchid.ui.VideoFragment

class MainActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        // Buttons Find
        val btnEvent = findViewById<Button>(R.id.btnEvent)
        val btnToday = findViewById<Button>(R.id.btnToday)
        val btnArtist = findViewById<Button>(R.id.btnArtist)
        val btnVideo = findViewById<Button>(R.id.btnVideo)

        // Default Fragment Load
        loadFragment(EventFragment())

        // Button Click Listeners
        btnEvent.setOnClickListener {
            loadFragment(EventFragment())
        }

        btnToday.setOnClickListener {
            loadFragment(TodayFragment())
        }

        btnArtist.setOnClickListener {
            loadFragment(ArtistFragment())
        }

        btnVideo.setOnClickListener {
            loadFragment(VideoFragment())
        }
    }

    // Fragment Load Function
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.eventContainer, fragment)
            .commit()
    }
}
