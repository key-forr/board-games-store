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
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.domain.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sessionManager: SessionManager

    private val viewModel: LoginViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        LoginViewModelFactory(userRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        if (sessionManager.isLoggedIn()) {
            navigateBasedOnRole(sessionManager.getUserRole())
            return
        }

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
            .requestIdToken(getString(R.string.web_client_id))
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
            val (success, userId, email, roleId) = result
            Log.d("LoginObserver", "Result: success=$success, roleId=$roleId")

            if (success) {
                sessionManager.saveUserSession(userId, email, roleId)

                Toast.makeText(requireContext(), "Успішний вхід!", Toast.LENGTH_SHORT).show()
                navigateBasedOnRole(roleId)
            } else {
                Toast.makeText(requireContext(), "Помилка входу!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateBasedOnRole(roleId: Long) {
        when (roleId) {
            1L -> findNavController().navigate(R.id.action_loginFragment_to_clientFragment)
            2L -> findNavController().navigate(R.id.action_loginFragment_to_adminFragment)
            else -> Toast.makeText(requireContext(), "Невідома роль!", Toast.LENGTH_SHORT).show()
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