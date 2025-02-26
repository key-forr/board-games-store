package com.example.tutor.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentLoginBinding
import com.example.tutor.ui.viewmodel.LoginViewModel
import com.example.tutor.ui.viewmodel.LoginViewModelFactory
import com.example.tutor.data.repository.UserRepository
import com.example.tutor.data.database.DbHelper

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        val userDao = DbHelper.getDb(requireContext()).getUserDao()
        val userRepository = UserRepository(userDao)
        LoginViewModelFactory(userRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Введіть email та пароль!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRegister.setOnClickListener {
            Log.d("Navigation", "Register button clicked")
            try {
                val navController = findNavController()
                Log.d("Navigation", "NavController found: $navController")
                val action = R.id.action_loginFragment_to_registerFragment
                Log.d("Navigation", "Action ID: $action")
                navController.navigate(action)
            } catch (e: Exception) {
                Log.e("Navigation", "Navigation failed", e)
                Toast.makeText(requireContext(), "Помилка навігації: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            val (success, roleId) = result
            if (success) {
                Toast.makeText(requireContext(), "Успішний вхід!", Toast.LENGTH_SHORT).show()
                when (roleId) {
                    1L -> findNavController().navigate(R.id.clientFragment) // Клієнт
                    2L -> findNavController().navigate(R.id.adminFragment)  // Адміністратор
                    else -> Toast.makeText(requireContext(), "Невідома роль!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Невірний email або пароль!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}