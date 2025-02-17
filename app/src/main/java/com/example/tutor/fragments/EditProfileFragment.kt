package com.example.tutor.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tutor.MainActivity
import com.example.tutor.R
import com.example.tutor.databinding.FragmentEditProfileBinding
import com.example.tutor.utils.BitmapUtils
import org.json.JSONArray

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val selectedImageBitmap = data?.data?.let { uri ->
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
            selectedImageBitmap?.let { saveAvatarToUser(it) }
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let { saveAvatarToUser(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val loggedInEmail = sharedPreferences.getString("loggedInUser", null)
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") == loggedInEmail) {
                binding.etFullName.setText(user.getString("fullName"))
                binding.etUsername.setText(user.getString("username"))
                binding.etEmail.setText(user.getString("email"))
                binding.etBirthDate.setText(user.getString("birthDate"))

                if (user.has("avatar")) {
                    try {
                        val avatarBase64 = user.getString("avatar")
                        val bitmap = BitmapUtils.base64ToBitmap(avatarBase64)
                        binding.ivAvatar.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.e("EditProfileFragment", "Error loading avatar", e)
                    }
                }

                break
            }
        }

        binding.btnChooseFromGallery.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryLauncher.launch(galleryIntent)
        }

        binding.btnTakePhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        }

        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }

        binding.btnGoBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnDeleteAccount.setOnClickListener {
            showDeleteConfirmationDialog()
            Log.d("DeleteAccount", usersArray.toString())
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action is irreversible.")
            .setPositiveButton("Yes") { _, _ -> deleteAccount() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)
        val loggedInEmail = sharedPreferences.getString("loggedInUser", null)

        if (loggedInEmail == null) {
            Toast.makeText(requireContext(), "No user found", Toast.LENGTH_SHORT).show()
            return
        }

        var userFound = false
        val newUsersArray = JSONArray()
        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") != loggedInEmail) {
                newUsersArray.put(user)
            } else {
                userFound = true
            }
        }

        if (!userFound) {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPreferences.edit()
            .putString("users", newUsersArray.toString())
            .remove("loggedInUser")
            .apply()

        Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()

        requireActivity().finish()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    private fun saveChanges() {
        val newFullName = binding.etFullName.text.toString().trim()
        val newUsername = binding.etUsername.text.toString().trim()
        val newEmail = binding.etEmail.text.toString().trim()
        val newBirthDate = binding.etBirthDate.text.toString().trim()

        if (newFullName.isEmpty() || newUsername.isEmpty() || newEmail.isEmpty() || newBirthDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)
        val loggedInEmail = sharedPreferences.getString("loggedInUser", null)

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") == loggedInEmail) {
                user.put("fullName", newFullName)
                user.put("username", newUsername)
                user.put("email", newEmail)
                user.put("birthDate", newBirthDate)
                break
            }
        }

        sharedPreferences.edit()
            .putString("users", usersArray.toString())
            .putString("loggedInUser", newEmail)
            .apply()

        Toast.makeText(requireContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.profileFragment)
    }

    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove("loggedInUser")
            .apply()

        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        requireActivity().finish()
    }

    private fun saveAvatarToUser(bitmap: Bitmap) {
        try {
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val usersJson = sharedPreferences.getString("users", "[]")
            val usersArray = JSONArray(usersJson)
            val loggedInEmail = sharedPreferences.getString("loggedInUser", null)

            val avatarBase64 = BitmapUtils.bitmapToBase64(bitmap)

            for (i in 0 until usersArray.length()) {
                val user = usersArray.getJSONObject(i)
                if (user.getString("email") == loggedInEmail) {
                    user.put("avatar", avatarBase64)
                    break
                }
            }

            sharedPreferences.edit()
                .putString("users", usersArray.toString())
                .apply()

            binding.ivAvatar.setImageBitmap(bitmap)
            Toast.makeText(requireContext(), "Avatar updated successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("EditProfileFragment", "Error saving avatar", e)
            Toast.makeText(requireContext(), "Error saving avatar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
