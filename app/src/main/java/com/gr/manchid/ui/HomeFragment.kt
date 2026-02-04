package com.gr.manchid.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.gr.manchid.R
import com.gr.manchid.data.CarouselItem
import com.gr.manchid.data.repository.AuthRepository
import com.gr.manchid.models.LoginState
import com.gr.manchid.models.LoginViewModel
import com.gr.manchid.models.LoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_home) {

    // ================= UI =================
    private lateinit var btnGoogle: MaterialButton
    private lateinit var btnLogout: MaterialButton
    private lateinit var progressBar: View
    private lateinit var viewPager: ViewPager2

    // ================= AUTH =================
    private lateinit var repository: AuthRepository

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(repository)
    }

    // ================= WEB CLIENT ID =================
    private val serverClientId by lazy {
        getString(R.string.default_web_client_id)
    }

    // ================= GOOGLE LAUNCHER =================
    private val googleSignInLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {

                    val account = task.getResult(ApiException::class.java)

                    account.idToken?.let {
                        viewModel.firebaseAuthWithGoogle(it)
                    }

                } catch (e: Exception) {

                    Log.e("GOOGLE_SIGN", e.message ?: "Google Sign Error")
                }
            }
        }

    // ================= CAROUSEL =================
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            viewPager.currentItem = viewPager.currentItem + 1
            handler.postDelayed(this, 4000)
        }
    }

    // ================= LIFECYCLE =================
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews(view)
        setupRepository()
        observeState()
        setupClick()
        setupCarousel()

        viewModel.checkCurrentUser()
    }

    // ================= REPOSITORY =================
    private fun setupRepository() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val credentialManager =
            CredentialManager.create(requireContext())
        repository = AuthRepository(firebaseAuth, credentialManager)
    }

    // ================= GOOGLE START =================
    private fun startGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(requireActivity(), gso)

        googleSignInLauncher.launch(client.signInIntent)
    }

    // ================= OBSERVE STATE =================
    private fun observeState() {

        viewModel.state.observe(viewLifecycleOwner) { state ->

            when (state) {

                is LoginState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    btnGoogle.visibility = View.GONE
                    btnLogout.visibility = View.GONE
                }

                is LoginState.LoggedIn -> {
                    progressBar.visibility = View.GONE
                    btnGoogle.visibility = View.GONE
                    btnLogout.visibility = View.VISIBLE
                }

                is LoginState.LoggedOut -> {
                    progressBar.visibility = View.GONE
                    btnGoogle.visibility = View.VISIBLE
                    btnLogout.visibility = View.GONE
                }

                is LoginState.Error -> {
                    progressBar.visibility = View.GONE
                    btnGoogle.visibility = View.VISIBLE
                    btnLogout.visibility = View.GONE
                }

                else -> Unit
            }
        }
    }

    // ================= CLICK =================
    private fun setupClick() {

        btnGoogle.setOnClickListener {
            startGoogleSignIn()
        }

        btnLogout.setOnClickListener {
            viewModel.signOut()
        }
    }

    // ================= VIEW BIND =================
    private fun bindViews(view: View) {

        btnGoogle = view.findViewById(R.id.btnGoogle)
        btnLogout = view.findViewById(R.id.btnLogout)
        progressBar = view.findViewById(R.id.progressBar)
        viewPager = view.findViewById(R.id.carouselViewPager)
    }

    // ================= CAROUSEL =================
    private fun setupCarousel() {

        val list = listOf(
            CarouselItem("Institute Feature","Promote Institute","Admission Leads","Student Reach","Brand Visibility"),
            CarouselItem("Local Event","Event Promotion","Ticket Booking","Local Audience","Sponsor Support"),
            CarouselItem("Sport Tournament","Team Registration","Live Updates","Match Scheduling","Prize Management"),
            CarouselItem("Artist","Profile Promotion","Show Booking","Fan Following","Event Invitation")
        )

        val adapter = CarouselAdapter(list)
        viewPager.adapter = adapter

        viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)

        viewPager.setPageTransformer { page, position ->
            val scale = 0.90f + (1 - kotlin.math.abs(position)) * 0.10f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.7f + (1 - kotlin.math.abs(position))
        }

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
