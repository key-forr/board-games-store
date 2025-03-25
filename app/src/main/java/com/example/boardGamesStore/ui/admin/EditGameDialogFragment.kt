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
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.databinding.DialogEditGameBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID

class EditGameDialogFragment : DialogFragment() {
    private var _binding: DialogEditGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var boardGameRepository: BoardGameRepository
    private var gameToEdit: BoardGame? = null
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
        setStyle(STYLE_NO_TITLE, 0)
        gameToEdit = arguments?.getParcelable(ARG_GAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        boardGameRepository = BoardGameRepository(database.boardGameDao())

        // Fill the form with the game data
        gameToEdit?.let { game ->
            binding.nameEditText.setText(game.name)
            binding.descriptionEditText.setText(game.description)
            binding.priceEditText.setText(game.price.toString())

            // Load existing image if available
            game.imageUrl?.let { imageUrl ->
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.imagePreview)
                binding.imagePreview.visibility = View.VISIBLE
                binding.addImageBtn.text = "Змінити зображення"
            }
        }

        binding.addImageBtn.setOnClickListener {
            openImageGallery()
        }

        binding.saveButton.setOnClickListener {
            updateGame()
        }

        binding.cancelButton.setOnClickListener {
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

    private fun updateGame() {
        val name = binding.nameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val priceStr = binding.priceEditText.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(requireContext(), "Заповніть усі обов'язкові поля", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val price = priceStr.toDouble()

            gameToEdit?.let { game ->
                lifecycleScope.launch {
                    val savedImagePath = selectedImageUri?.let { saveImageToLocalStorage(it) }

                    val updatedGame = game.copy(
                        name = name,
                        description = description,
                        price = price,
                        imageUrl = savedImagePath ?: game.imageUrl,
                        updatedAt = Date()
                    )

                    boardGameRepository.updateBoardGame(updatedGame)
                    dismiss()
                    Toast.makeText(requireContext(), "Гру успішно оновлено", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Введіть коректну ціну", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "EditGameDialogFragment"
        private const val ARG_GAME = "arg_game"

        fun newInstance(game: BoardGame): EditGameDialogFragment {
            return EditGameDialogFragment().apply {
                arguments = bundleOf(ARG_GAME to game)
            }
        }
    }
}