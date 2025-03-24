package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.databinding.DialogEditGameBinding
import kotlinx.coroutines.launch
import java.util.Date

class EditGameDialogFragment : DialogFragment() {
    private var _binding: DialogEditGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var boardGameRepository: BoardGameRepository
    private var gameToEdit: BoardGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use DialogFragment.STYLE_NO_TITLE to remove the title
        setStyle(STYLE_NO_TITLE, 0)

        // Get the game from arguments
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
            binding.imageUrlEditText.setText(game.imageUrl ?: "")
        }

        binding.saveButton.setOnClickListener {
            updateGame()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // Встановлюємо ширину діалогу (ширше, але не на весь екран)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun updateGame() {
        val name = binding.nameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val priceStr = binding.priceEditText.text.toString().trim()
        val imageUrl = binding.imageUrlEditText.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(requireContext(), "Заповніть усі обов'язкові поля", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val price = priceStr.toDouble()

            gameToEdit?.let { game ->
                val updatedGame = game.copy(
                    name = name,
                    description = description,
                    price = price,
                    imageUrl = if (imageUrl.isEmpty()) null else imageUrl,
                    updatedAt = Date()
                )

                lifecycleScope.launch {
                    boardGameRepository.updateBoardGame(updatedGame)
                    dismiss()
                    Toast.makeText(requireContext(), "Гру успішно оновлено", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Введіть коректну ціну", Toast.LENGTH_SHORT).show()
        }
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