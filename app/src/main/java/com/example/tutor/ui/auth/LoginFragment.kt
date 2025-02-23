package com.example.tutor.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
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
        } catch (e: Exception) {
            Log.e("Navigation", "Error setting up click listener", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
