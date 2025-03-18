package com.example.boardGamesStore.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.repository.UserRepository
import com.example.boardGamesStore.databinding.FragmentProfileBinding
import com.example.boardGamesStore.domain.SessionManager
import com.example.boardGamesStore.extensions.navigateToLoginGlobally
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var userRepository: UserRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        loadUserData()

        binding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun loadUserData() {
        val userId = sessionManager.getUserId()
        val userEmail = sessionManager.getUserEmail() ?: ""

        viewLifecycleOwner.lifecycleScope.launch {
            val user = userRepository.getUserById(userId)
            user?.let {
                binding.usernameTv.text = it.username
                binding.emailTv.text = userEmail
            }
        }
    }

    private fun logout() {
        sessionManager.logout()

        navigateToLoginGlobally()
    }
}