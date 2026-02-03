package com.gr.manchid.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gr.manchid.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            viewPager.currentItem = viewPager.currentItem + 1
            handler.postDelayed(this, 2500)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TEST_FRAGMENT", "HomeFragment Loaded")

        viewPager = view.findViewById(R.id.carouselViewPager)

        val list = listOf(
            "Institute Feature",
            "Local Event",
            "Sport Tournament",
            "Artist"
        )

        val adapter = CarouselAdapter(list)
        viewPager.adapter = adapter

        viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)

        viewPager.setPageTransformer { page, position ->
            val scale = 0.85f + (1 - Math.abs(position)) * 0.15f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.5f + (1 - Math.abs(position))
        }

        handler.postDelayed(runnable, 2500)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 2500)
    }
}
