package com.gr.manchid.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gr.manchid.R
import com.gr.manchid.data.CarouselItem

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewPager: ViewPager2

    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            viewPager.currentItem = viewPager.currentItem + 1
            handler.postDelayed(this, 4000) // Speed control (4 sec)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("HOME_FRAGMENT", "Loaded")

        viewPager = view.findViewById(R.id.carouselViewPager)

        // 🔥 DATA LIST
        val list = listOf(
            CarouselItem(
                "Institute Feature",
                "Promote Institute",
                "Admission Leads",
                "Student Reach",
                "Brand Visibility"
            ),
            CarouselItem(
                "Local Event",
                "Event Promotion",
                "Ticket Booking",
                "Local Audience",
                "Sponsor Support"
            ),
            CarouselItem(
                "Sport Tournament",
                "Team Registration",
                "Live Updates",
                "Match Scheduling",
                "Prize Management"
            ),
            CarouselItem(
                "Artist",
                "Profile Promotion",
                "Show Booking",
                "Fan Following",
                "Event Invitation"
            )
        )

        val adapter = CarouselAdapter(list)
        viewPager.adapter = adapter

        // Infinite feel start
        viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)

        // Zoom Animation
        viewPager.setPageTransformer { page, position ->
            val scale = 0.90f + (1 - kotlin.math.abs(position)) * 0.10f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.7f + (1 - kotlin.math.abs(position))
        }

        // 🔥 IMPORTANT — Layout ready hone ke baad auto slide start
        viewPager.post {
            handler.postDelayed(runnable, 4000)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        viewPager.post {
            handler.postDelayed(runnable, 4000)
        }
    }
}
