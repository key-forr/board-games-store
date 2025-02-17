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
import org.json.JSONArray

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

        checkLoggedInUser()

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val enteredEmail = binding.etEmail.text.toString().trim()
            val enteredPassword = binding.etPassword.text.toString().trim()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (validateUser(enteredEmail, enteredPassword)) {
                ValidationResult.SUCCESS -> {
                    Toast.makeText(requireContext(), "Вхід успішний!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), SecondActivity::class.java)
                    intent.putExtra("userEmail", enteredEmail)
                    startActivity(intent)
                    requireActivity().finish()
                }
                ValidationResult.USER_NOT_FOUND -> {
                    Toast.makeText(requireContext(), "Користувача не знайдено", Toast.LENGTH_SHORT).show()
                }
                ValidationResult.INVALID_PASSWORD -> {
                    Toast.makeText(requireContext(), "Неправильний пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkLoggedInUser() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val loggedInEmail = sharedPreferences.getString("loggedInUser", null)

        if (loggedInEmail != null && isUserExists(loggedInEmail)) {
            val intent = Intent(requireActivity(), SecondActivity::class.java)
            intent.putExtra("userEmail", loggedInEmail)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun isUserExists(email: String): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") == email) {
                return true
            }
        }

        return false
    }

    private enum class ValidationResult {
        SUCCESS,
        USER_NOT_FOUND,
        INVALID_PASSWORD
    }

    private fun validateUser(email: String, password: String): ValidationResult {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") == email) {
                return if (user.getString("password") == password) {
                    sharedPreferences.edit()
                        .putString("loggedInUser", email)
                        .apply()
                    ValidationResult.SUCCESS
                } else {
                    ValidationResult.INVALID_PASSWORD
                }
            }
        }
        return ValidationResult.USER_NOT_FOUND
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}