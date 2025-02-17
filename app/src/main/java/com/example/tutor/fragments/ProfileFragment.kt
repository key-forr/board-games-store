package com.example.tutor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentProfileBinding
import com.example.tutor.utils.BitmapUtils
import org.json.JSONArray

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val loggedInEmail = sharedPreferences.getString("loggedInUser", null)
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        binding.btnEditProfile.setOnClickListener {
            try {
                findNavController().navigate(R.id.editProfileFragment)
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Navigation error", e)
                e.printStackTrace()
            }
        }

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") == loggedInEmail) {
                binding.tvFullName.text = user.getString("fullName")
                binding.tvUsername.text = user.getString("username")
                binding.tvEmail.text = user.getString("email")
                binding.tvBirthDate.text = user.getString("birthDate")

                if (user.has("avatar")) {
                    try {
                        val avatarBase64 = user.getString("avatar")
                        val bitmap = BitmapUtils.base64ToBitmap(avatarBase64)
                        binding.ivAvatar.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.e("ProfileFragment", "Error loading avatar", e)
                    }
                }

                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


