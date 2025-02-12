package com.example.tutor.fragments
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentEditProfileBinding
import com.example.tutor.utils.BitmapUtils

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
            selectedImageBitmap?.let { saveAvatarToSharedPrefs(it) }
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let { saveAvatarToSharedPrefs(it) }
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

        val sharedPrefs = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedAvatar = sharedPrefs.getString("avatar", null)
        savedAvatar?.let {
            val bitmap = BitmapUtils.base64ToBitmap(it)
            binding.ivAvatar.setImageBitmap(bitmap)
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

        binding.btnGoBack.setOnClickListener {
            try {
                findNavController().navigate(R.id.profileFragment)
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Navigation error", e)
                e.printStackTrace()
            }
        }
    }

    private fun saveAvatarToSharedPrefs(bitmap: Bitmap) {
        val sharedPrefs = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val base64Image = BitmapUtils.bitmapToBase64(bitmap)
        sharedPrefs.edit().putString("avatar", base64Image).apply()
        binding.ivAvatar.setImageBitmap(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}