package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.data.repository.CartRepository
import com.example.boardGamesStore.databinding.FragmentAdminGamesListBinding
import com.example.boardGamesStore.databinding.FragmentListBinding
import com.example.boardGamesStore.domain.SessionManager
import com.example.boardGamesStore.ui.adapter.BoardGameAdapter
import com.example.boardGamesStore.ui.admin.AddGameDialogFragment
import com.example.boardGamesStore.ui.admin.AdminGamesAdapter
import com.example.boardGamesStore.ui.viewmodel.BoardGameViewModel
import com.example.boardGamesStore.ui.viewmodel.BoardGameViewModelFactory
import com.example.boardGamesStore.ui.viewmodel.CartViewModel
import com.example.boardGamesStore.ui.viewmodel.CartViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment : Fragment() {
    private lateinit var boardGameAdapter: BoardGameAdapter
    private lateinit var binding: FragmentListBinding
    private lateinit var boardGameRepository: BoardGameRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var boardGameViewModel: BoardGameViewModel
    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        sessionManager = SessionManager(requireContext())

        val database = AppDatabase.getDatabase(requireContext())
        boardGameRepository = BoardGameRepository(database.boardGameDao())
        val cartRepository = CartRepository(database.cartDao())

        val boardGameViewModelFactory = BoardGameViewModelFactory(boardGameRepository)
        boardGameViewModel = ViewModelProvider(this, boardGameViewModelFactory)[BoardGameViewModel::class.java]

        val cartViewModelFactory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        cartViewModel.setUserId(sessionManager.getUserId())

        boardGameAdapter = BoardGameAdapter { boardGame ->
            showGameDetailsDialog(boardGame)
        }

        binding.recyclerBoardGames.adapter = boardGameAdapter

        setupPriceRangeSlider()
        setupSearchView()

        boardGameViewModel.allBoardGames.observe(viewLifecycleOwner) { boardGames ->
            val activeBoardGames = boardGames.filter { it.isActive }
            boardGameAdapter.submitList(activeBoardGames)
        }

        return binding.root
    }

    private fun setupPriceRangeSlider() {
        // Отримуємо діапазон цін
        CoroutineScope(Dispatchers.Main).launch {
            val (minPrice, maxPrice) = boardGameRepository.getPriceRange()

            binding.priceRangeSlider.apply {
                valueFrom = minPrice.toFloat()
                valueTo = maxPrice.toFloat()
                values = listOf(minPrice.toFloat(), maxPrice.toFloat())
            }

            // Додаємо listener для slider
            binding.priceRangeSlider.addOnChangeListener { slider, _, _ ->
                val currentMinPrice = slider.values[0]
                val currentMaxPrice = slider.values[1]

                filterGamesByPrice(currentMinPrice.toDouble(), currentMaxPrice.toDouble())
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

    private fun searchGames(query: String) {
        boardGameRepository.searchBoardGames(query).observe(viewLifecycleOwner) { games ->
            val activeBoardGames = games.filter { it.isActive }
            updateGamesList(activeBoardGames)
        }
    }

    private fun filterGamesByPrice(minPrice: Double, maxPrice: Double) {
        boardGameRepository.filterBoardGamesByPrice(minPrice, maxPrice)
            .observe(viewLifecycleOwner) { games ->
                val activeBoardGames = games.filter { it.isActive }
                updateGamesList(activeBoardGames)
            }
    }

    private fun updateGamesList(games: List<BoardGame>) {
        binding.progressBar.visibility = View.GONE

        if (games.isEmpty()) {
            binding.recyclerBoardGames.visibility = View.GONE
        } else {
            binding.recyclerBoardGames.visibility = View.VISIBLE
            boardGameAdapter.submitList(games)
        }
    }

    private fun showGameDetailsDialog(boardGame: BoardGame) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_game_detail, null)

        val imageView = view.findViewById<ImageView>(R.id.iv_dialog_game)
        val nameTextView = view.findViewById<TextView>(R.id.tv_dialog_game_name)
        val priceTextView = view.findViewById<TextView>(R.id.tv_dialog_price)
        val descriptionTextView = view.findViewById<TextView>(R.id.tv_dialog_description)
        val quantityTextView = view.findViewById<TextView>(R.id.tv_dialog_quantity)
        val btnDecrease = view.findViewById<ImageButton>(R.id.btn_dialog_decrease)
        val btnIncrease = view.findViewById<ImageButton>(R.id.btn_dialog_increase)
        val btnAddToCart = view.findViewById<Button>(R.id.btn_add_to_cart)

        boardGame.imageUrl?.let { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.placeholder_game)
                .error(R.drawable.error_image)
                .into(imageView)
        } ?: run {
            imageView.setImageResource(R.drawable.placeholder_game)
        }

        nameTextView.text = boardGame.name
        priceTextView.text = String.format("₴%.2f", boardGame.price)
        descriptionTextView.text = boardGame.description

        var quantity = 1
        quantityTextView.text = quantity.toString()

        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        btnIncrease.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
        }

        btnAddToCart.setOnClickListener {
            boardGame.id?.let { gameId ->
                cartViewModel.addToCart(gameId, quantity)
                dialog.dismiss()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }
}