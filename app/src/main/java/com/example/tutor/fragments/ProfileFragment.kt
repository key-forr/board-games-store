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

        val sharedPrefs = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedAvatar = sharedPrefs.getString("avatar", null)
        savedAvatar?.let {
            val bitmap = BitmapUtils.base64ToBitmap(it)
            binding.ivAvatar.setImageBitmap(bitmap)
        }

        binding.btnEditProfile.setOnClickListener {
            try {
                findNavController().navigate(R.id.editProfileFragment)
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Navigation error", e)
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}