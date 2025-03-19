package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.databinding.FragmentAddGameBinding
import kotlinx.coroutines.launch
import java.util.Date

class AddGameFragment : Fragment() {
    private lateinit var binding: FragmentAddGameBinding
    private lateinit var boardGameRepository: BoardGameRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardGameRepository = BoardGameRepository(AppDatabase.getDatabase(requireContext()).boardGameDao())

        binding.saveBtn.setOnClickListener {
            val name = binding.nameEt.text.toString()
            val description = binding.descriptionEt.text.toString()
            val priceText = binding.priceEt.text.toString()
            val imageUrl = binding.imageUrlEt.text.toString()

            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(requireContext(), "Заповніть всі обов'язкові поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull()
            if (price == null || price <= 0) {
                Toast.makeText(requireContext(), "Введіть коректну ціну", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
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
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Помилка при додаванні гри", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}