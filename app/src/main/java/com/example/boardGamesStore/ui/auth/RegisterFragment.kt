package com.example.boardGamesStore.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.boardGamesStore.R
import com.example.boardGamesStore.databinding.FragmentRegisterBinding
import com.example.boardGamesStore.domain.RegisterUserUseCase
import com.example.boardGamesStore.ui.viewmodel.RegisterViewModel
import com.example.boardGamesStore.ui.viewmodel.RegisterViewModelFactory
import com.example.boardGamesStore.data.repository.UserRepository
import com.example.boardGamesStore.data.database.AppDatabase

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        RegisterViewModelFactory(RegisterUserUseCase(userRepository))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signUpBtn.setOnClickListener {
            val username = binding.usernameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()

            if (password == confirmPassword) {
                viewModel.register(username, email, password)
            } else {
                Toast.makeText(requireContext(), "Паролі не співпадають", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registrationResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Реєстрація успішна!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)
            } else {
                Toast.makeText(requireContext(), "Помилка реєстрації!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}