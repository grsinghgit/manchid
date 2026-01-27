package com.gr.manchid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gr.manchid.utils.MyVar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        // Data class object receive
        val myVar = intent.getParcelableExtra<MyVar>("MY_DATA")

// yturl ko ek normal String variable me daal do
        val ytUrl: String = myVar?.yturl ?: ""

        Toast.makeText(this, "URL = $ytUrl", Toast.LENGTH_LONG).show()



        val videoId = extractVideoId(ytUrl)


       // if (videoId.isEmpty()) {
          //  Toast.makeText(
           //     this,
            //    "Please enter a valid YouTube URL",
               // Toast.LENGTH_SHORT
           // ).show()
           // return
       // }
        //val dd: String = "2kpXoUP5Cio"



//call yoyube uyube
        val youtubePlayerView =
            findViewById<YouTubePlayerView>(R.id.youtubePlayerView)
        utube.setupPlayer(lifecycle,youtubePlayerView,videoId)




    }

    // private fun space
    // youtube vid extractor function
    private fun extractVideoId(input: String): String {
        val url = input.trim()

        return when {
            // watch?v=
            url.contains("watch?v=") ->
                url.substringAfter("watch?v=").substringBefore("&")

            // youtu.be/
            url.contains("youtu.be/") ->
                url.substringAfter("youtu.be/").substringBefore("?")

            // shorts
            url.contains("/shorts/") ->
                url.substringAfter("/shorts/").substringBefore("?")

            // live
            url.contains("/live/") ->
                url.substringAfter("/live/").substringBefore("?")

            // direct videoId
            url.length == 11 && !url.contains("/") ->
                url

            else -> ""
        }
    }




}