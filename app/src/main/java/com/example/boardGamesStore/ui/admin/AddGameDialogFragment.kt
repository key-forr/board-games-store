package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.databinding.FragmentAddGameDialogBinding
import kotlinx.coroutines.launch
import java.util.Date

class AddGameDialogFragment : DialogFragment() {
    private var _binding: FragmentAddGameDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var boardGameRepository: BoardGameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Встановлення стилю для діалогу, щоб він був як модальне вікно
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddGameDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardGameRepository = BoardGameRepository(AppDatabase.getDatabase(requireContext()).boardGameDao())

        binding.saveBtn.setOnClickListener {
            saveGame()
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun saveGame() {
        val name = binding.nameEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val priceText = binding.priceEt.text.toString()
        val imageUrl = binding.imageUrlEt.text.toString()

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(requireContext(), "Заповніть всі обов'язкові поля", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        if (price == null || price <= 0) {
            Toast.makeText(requireContext(), "Введіть коректну ціну", Toast.LENGTH_SHORT).show()
            return
        }

        val boardGame = BoardGame(
            name = name,
            description = description,
            price = price,
            imageUrl = if (imageUrl.isEmpty()) null else imageUrl,
            createdAt = Date()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val id = boardGameRepository.insertBoardGame(boardGame)
            if (id > 0) {
                Toast.makeText(requireContext(), "Гру успішно додано", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Помилка при додаванні гри", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Налаштування розмірів діалогу
        dialog?.window?.let { window ->
            // Встановлення ширини діалогу з відступами зліва та справа
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)

            // Додавання відступів зліва та справа
            val displayMetrics = resources.displayMetrics
            val dialogWidth = (displayMetrics.widthPixels * 0.9).toInt() // 90% ширини екрану
            val params = window.attributes
            params.width = dialogWidth
            window.attributes = params
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