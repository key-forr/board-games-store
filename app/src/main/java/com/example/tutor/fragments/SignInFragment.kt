package com.example.tutor.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.SecondActivity
import com.example.tutor.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val enteredEmail = binding.etEmail.text.toString().trim()
            val enteredPassword = binding.etPassword.text.toString().trim()

            if (validateUser(enteredEmail, enteredPassword)) {
                Toast.makeText(requireContext(), "Вхід успішний!", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireActivity(), SecondActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Неправильний email або пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUser(email: String, password: String): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)

        return email == savedEmail && password == savedPassword
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
