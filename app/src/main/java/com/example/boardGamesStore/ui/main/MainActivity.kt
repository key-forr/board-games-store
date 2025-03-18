package com.example.boardGamesStore.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.boardGamesStore.R
import com.example.boardGamesStore.databinding.ActivityMainBinding
import com.example.boardGamesStore.domain.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        sessionManager = SessionManager(this)

        setupLogoutListener()
    }

    private fun setupLogoutListener() {
    }

    fun logoutAndNavigateToLogin() {
        sessionManager.logout()
        navController.navigate(R.id.action_global_loginFragment)
    }
}