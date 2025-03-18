package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.CartWithBoardGames
import com.example.boardGamesStore.data.entity.Order
import com.example.boardGamesStore.data.entity.OrderItem
import com.example.boardGamesStore.data.repository.CartRepository
import com.example.boardGamesStore.data.repository.OrderRepository
import com.example.boardGamesStore.databinding.FragmentCheckoutBinding
import com.example.boardGamesStore.domain.SessionManager
import kotlinx.coroutines.launch
import java.util.Date

class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var cartRepository: CartRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var sessionManager: SessionManager
    private var cartItems: List<CartWithBoardGames> = emptyList()
    private var totalPrice: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        cartRepository = CartRepository(database.cartDao())
        orderRepository = OrderRepository(database.orderDao())
        sessionManager = SessionManager(requireContext())

        val userId = sessionManager.getUserId()

        cartRepository.getCartWithBoardGames(userId).observe(viewLifecycleOwner) { items ->
            cartItems = items
            updateTotal()
        }

        binding.placeOrderBtn.setOnClickListener {
            placeOrder()
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateTotal() {
        totalPrice = cartItems.sumOf { it.boardGame.price * it.cartItem.quantity }
        binding.totalPriceTv.text = String.format("%.2f грн", totalPrice)
    }

    private fun placeOrder() {
        val address = binding.addressEt.text.toString()
        val phone = binding.phoneEt.text.toString()

        if (address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "Заповніть всі поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Кошик порожній", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = sessionManager.getUserId()

        viewLifecycleOwner.lifecycleScope.launch {
            val order = Order(
                userId = userId,
                totalPrice = totalPrice,
                status = "PENDING",
                deliveryAddress = address,
                phoneNumber = phone,
                createdAt = Date()
            )

            val orderId = orderRepository.createOrder(order)

            cartItems.forEach { cartWithGame ->
                val orderItem = OrderItem(
                    orderId = orderId,
                    boardGameId = cartWithGame.boardGame.id!!,
                    quantity = cartWithGame.cartItem.quantity,
                    pricePerUnit = cartWithGame.boardGame.price
                )
                orderRepository.addOrderItem(orderItem)
            }

            cartRepository.clearCart(userId)

            Toast.makeText(requireContext(), "Замовлення успішно оформлено!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_checkoutFragment_to_ordersFragment)
        }
    }
}