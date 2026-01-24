package com.gr.manchid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



//call yoyube uyube
        val youtubePlayerView =
            findViewById<YouTubePlayerView>(R.id.youtubePlayerView)
        utube.setupPlayer(lifecycle,youtubePlayerView, "2kpXoUP5Cio")




    }

}