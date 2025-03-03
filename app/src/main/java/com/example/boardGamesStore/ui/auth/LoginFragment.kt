package com.example.boardGamesStore.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.boardGamesStore.R
import com.example.boardGamesStore.databinding.FragmentLoginBinding
import com.example.boardGamesStore.ui.viewmodel.LoginViewModel
import com.example.boardGamesStore.ui.viewmodel.LoginViewModelFactory
import com.example.boardGamesStore.data.repository.UserRepository
import com.example.boardGamesStore.data.database.DbHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

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

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Введіть email та пароль!", Toast.LENGTH_SHORT).show()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("317262166736-i7osc9rsrmi5nara8b0mc906nnn7d2jk.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.buttonGoogleSignIn.setOnClickListener {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        }

        binding.goToRegisterActivityTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            val (success, roleId) = result
            Log.d("LoginObserver", "Result: success=$success, roleId=$roleId")

            if (success) {
                Toast.makeText(requireContext(), "Успішний вхід!", Toast.LENGTH_SHORT).show()
                when (roleId) {
                    1L -> findNavController().navigate(R.id.clientFragment)
                    2L -> findNavController().navigate(R.id.adminFragment)
                    else -> Toast.makeText(requireContext(), "Невідома роль!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Помилка входу!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val email = account.email!!
                val googleId = account.id!!
                val photoUrl = account.photoUrl?.toString()

                Log.d("GoogleSignIn", "Успішний вхід: $email, ID: $googleId")

                viewModel.loginWithGoogle(email, googleId, photoUrl)
            } else {
                Log.e("GoogleSignIn", "Google Account is null")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed with code: ${e.statusCode}", e)
        }
    }
}
