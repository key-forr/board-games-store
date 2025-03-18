package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        loadGames()

        binding.addGameFab.setOnClickListener {
            findNavController().navigate(R.id.action_adminGamesListFragment_to_addGameFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = AdminGamesAdapter(
            onEditClick = { game ->
                },
            onDeleteClick = { game ->
                deleteGame(game)
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

    private fun deleteGame(game: BoardGame) {
        viewLifecycleOwner.lifecycleScope.launch {
            game.id?.let { id ->
                boardGameRepository.deleteBoardGame(id)
            }
        }
    }
}