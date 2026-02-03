package com.gr.manchid.ui.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gr.manchid.R
import androidx.lifecycle.ViewModelProvider


class PostFragment : Fragment(R.layout.fragment_post) {

    private lateinit var viewModel: PostViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TEST_FRAGMENT", "PostFragment Loaded")


        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        viewModel.postsLiveData.observe(viewLifecycleOwner) {

            Log.d("POST_DATA", it.toString())

        }

        viewModel.loadPost()
    }
}
