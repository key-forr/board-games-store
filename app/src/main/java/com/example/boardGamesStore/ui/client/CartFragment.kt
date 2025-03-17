// ui/client/CartFragment.kt
package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.repository.CartRepository
import com.example.boardGamesStore.domain.SessionManager
import com.example.boardGamesStore.ui.adapter.CartAdapter
import com.example.boardGamesStore.ui.viewmodel.CartViewModel
import com.example.boardGamesStore.ui.viewmodel.CartViewModelFactory

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyCartTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = view.findViewById(R.id.recycler_cart)
        emptyCartTextView = view.findViewById(R.id.tv_empty_cart)
        totalTextView = view.findViewById(R.id.tv_cart_total)
        checkoutButton = view.findViewById(R.id.btn_checkout)

        sessionManager = SessionManager(requireContext())

        // Ініціалізація ViewModel
        val database = AppDatabase.getDatabase(requireContext())
        val cartRepository = CartRepository(database.cartItemDao())
        val cartViewModelFactory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        // Встановлення ID користувача для CartViewModel
        cartViewModel.setUserId(sessionManager.getUserId())

        // Налаштування адаптера
        cartAdapter = CartAdapter(
            onItemClick = { cartWithBoardGame ->
                // Обробка кліку на елемент кошика (якщо потрібно)
            },
            onUpdateQuantity = { boardGameId, newQuantity ->
                cartViewModel.updateCartItemQuantity(boardGameId, newQuantity)
            },
            onRemoveItem = { boardGameId ->
                cartViewModel.removeFromCart(boardGameId)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Спостереження за змінами даних
        cartViewModel.getCartWithBoardGames().observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)

            if (cartItems.isEmpty()) {
                emptyCartTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                totalTextView.text = "₴0.00"
            } else {
                emptyCartTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                // Обчислення загальної вартості
                val total = cartItems.sumOf { it.boardGame.price * it.cartItem.quantity }
                totalTextView.text = String.format("₴%.2f", total)
            }
        }

        checkoutButton.setOnClickListener {
            // Тут буде логіка оформлення замовлення
        }

        return view
    }
}