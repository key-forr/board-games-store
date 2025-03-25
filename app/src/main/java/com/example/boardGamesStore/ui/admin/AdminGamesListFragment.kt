package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.databinding.FragmentAdminGamesListBinding
import kotlinx.coroutines.launch

class AdminGamesListFragment : Fragment() {
    private lateinit var binding: FragmentAdminGamesListBinding
    private lateinit var boardGameRepository: BoardGameRepository
    private lateinit var adapter: AdminGamesAdapter
    private var minPrice = 0.0
    private var maxPrice = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        boardGameRepository = BoardGameRepository(database.boardGameDao())

        setupRecyclerView()
        setupPriceRangeSlider()
        setupSearchView()
        loadGames()

        binding.addGameFab.setOnClickListener {
            // Замість навігації використовуємо діалог
            AddGameDialogFragment.newInstance().show(
                parentFragmentManager,
                AddGameDialogFragment.TAG
            )
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupPriceRangeSlider() {
        viewLifecycleOwner.lifecycleScope.launch {
            val priceRange = boardGameRepository.getPriceRange()
            minPrice = priceRange.first
            maxPrice = priceRange.second

            binding.priceRangeSlider.apply {
                valueFrom = minPrice.toFloat()
                valueTo = maxPrice.toFloat()
                values = listOf(minPrice.toFloat(), maxPrice.toFloat())

                addOnChangeListener { _, _, _ ->
                    filterGamesByPrice()
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchGames(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchGames(it) }
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = AdminGamesAdapter(
            onEditClick = { game ->
                EditGameDialogFragment.newInstance(game).show(
                    parentFragmentManager,
                    EditGameDialogFragment.TAG
                )
            },
            onDeleteClick = { game ->
                deactivateGame(game)  // Змінено на деактивацію замість видалення
            }
        )

        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AdminGamesListFragment.adapter
        }
    }

    private fun loadGames() {
        boardGameRepository.allBoardGames.observe(viewLifecycleOwner) { games ->
            if (games.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.gamesRecyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.gamesRecyclerView.visibility = View.VISIBLE
                adapter.submitList(games)
            }
        }
    }

    // Новий метод для деактивації гри замість видалення
    private fun deactivateGame(game: BoardGame) {
        viewLifecycleOwner.lifecycleScope.launch {
            game.id?.let { id ->
                boardGameRepository.deactivateBoardGame(id)
            }
        }
    }

    private fun searchGames(query: String) {
        boardGameRepository.searchBoardGames(query).observe(viewLifecycleOwner) { games ->
            updateGamesList(games)
        }
    }

    private fun filterGamesByPrice() {
        val currentValues = binding.priceRangeSlider.values
        val filteredMinPrice = currentValues[0].toDouble()
        val filteredMaxPrice = currentValues[1].toDouble()

        boardGameRepository.filterBoardGamesByPrice(filteredMinPrice, filteredMaxPrice)
            .observe(viewLifecycleOwner) { games ->
                updateGamesList(games)
            }
    }

    private fun updateGamesList(games: List<BoardGame>) {
        if (games.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.gamesRecyclerView.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.gamesRecyclerView.visibility = View.VISIBLE
            adapter.submitList(games)
        }
    }
}