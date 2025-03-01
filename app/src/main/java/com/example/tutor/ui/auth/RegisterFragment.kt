package com.example.tutor.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentRegisterBinding
import com.example.tutor.domain.RegisterUserUseCase
import com.example.tutor.ui.viewmodel.RegisterViewModel
import com.example.tutor.ui.viewmodel.RegisterViewModelFactory
import com.example.tutor.data.repository.UserRepository
import com.example.tutor.data.database.DbHelper

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        val userDao = DbHelper.getDb(requireContext()).getUserDao()
        val userRepository = UserRepository(userDao)
        RegisterViewModelFactory(RegisterUserUseCase(userRepository))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

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