package com.example.boardGamesStore.ui.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.databinding.FragmentAddGameDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID

class AddGameDialogFragment : DialogFragment() {
    private var _binding: FragmentAddGameDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var boardGameRepository: BoardGameRepository
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let { uri ->
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .into(binding.imagePreview)
                binding.imagePreview.visibility = View.VISIBLE
                binding.addImageBtn.text = "Змінити зображення"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddGameDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardGameRepository = BoardGameRepository(AppDatabase.getDatabase(requireContext()).boardGameDao())

        binding.addImageBtn.setOnClickListener {
            openImageGallery()
        }

        binding.saveBtn.setOnClickListener {
            saveGame()
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun openImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private suspend fun saveImageToLocalStorage(imageUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val fileName = "game_image_${UUID.randomUUID()}.jpg"
                val file = File(requireContext().filesDir, fileName)

                inputStream?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

                file.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun saveGame() {
        val name = binding.nameEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val priceText = binding.priceEt.text.toString()

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(requireContext(), "Заповніть всі обов'язкові поля", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        if (price == null || price <= 0) {
            Toast.makeText(requireContext(), "Введіть коректну ціну", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val savedImagePath = selectedImageUri?.let { saveImageToLocalStorage(it) }

            val boardGame = BoardGame(
                name = name,
                description = description,
                price = price,
                imageUrl = savedImagePath,
                createdAt = Date()
            )

            val id = boardGameRepository.insertBoardGame(boardGame)
            if (id > 0) {
                Toast.makeText(requireContext(), "Гру успішно додано", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Помилка при додаванні гри", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddGameDialogFragment"

        fun newInstance(): AddGameDialogFragment {
            return AddGameDialogFragment()
        }
    }
}