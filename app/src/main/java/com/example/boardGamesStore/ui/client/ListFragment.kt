// ui/client/ListFragment.kt
package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.data.repository.CartRepository
import com.example.boardGamesStore.domain.SessionManager
import com.example.boardGamesStore.ui.adapter.BoardGameAdapter
import com.example.boardGamesStore.ui.viewmodel.BoardGameViewModel
import com.example.boardGamesStore.ui.viewmodel.BoardGameViewModelFactory
import com.example.boardGamesStore.ui.viewmodel.CartViewModel
import com.example.boardGamesStore.ui.viewmodel.CartViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var boardGameViewModel: BoardGameViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var boardGameAdapter: BoardGameAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_board_games)

        sessionManager = SessionManager(requireContext())

        // Ініціалізація ViewModel
        val database = AppDatabase.getDatabase(requireContext())
        val boardGameRepository = BoardGameRepository(database.boardGameDao())
        val cartRepository = CartRepository(database.cartItemDao())

        val boardGameViewModelFactory = BoardGameViewModelFactory(boardGameRepository)
        boardGameViewModel = ViewModelProvider(this, boardGameViewModelFactory)[BoardGameViewModel::class.java]

        val cartViewModelFactory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        // Встановлення ID користувача для CartViewModel
        cartViewModel.setUserId(sessionManager.getUserId())

        // Налаштування адаптера
        boardGameAdapter = BoardGameAdapter { boardGame ->
            showGameDetailsDialog(boardGame)
        }

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = boardGameAdapter
        }

        // Спостереження за змінами даних
        boardGameViewModel.allBoardGames.observe(viewLifecycleOwner) { boardGames ->
            boardGameAdapter.submitList(boardGames)
        }

        return view
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

        // Встановлення даних
        boardGame.imageUrl?.let { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.placeholder_game)
                .error(R.drawable.error_image)
                .into(imageView)
        } ?: run {
            imageView.setImageResource(R.drawable.placeholder_game)
        }

        // ui/client/ListFragment.kt (продовження)
        nameTextView.text = boardGame.name
        priceTextView.text = String.format("₴%.2f", boardGame.price)
        descriptionTextView.text = boardGame.description

        var quantity = 1
        quantityTextView.text = quantity.toString()

        // Обробка кліків на кнопки
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
            val userId = sessionManager.getUserId()
            boardGame.id?.let { gameId ->
                cartViewModel.addToCart(gameId, quantity)
                dialog.dismiss()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }
}