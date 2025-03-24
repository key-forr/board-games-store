package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.entity.OrderWithItems
import com.example.boardGamesStore.data.repository.OrderRepository
import com.example.boardGamesStore.databinding.FragmentOrderDetailBinding
import com.example.boardGamesStore.ui.adapter.OrderItemsAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderRepository = OrderRepository(AppDatabase.getDatabase(requireContext()).orderDao())

        setupRecyclerView()
        loadOrderDetails()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        orderItemsAdapter = OrderItemsAdapter()
        binding.orderItemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderItemsAdapter
        }
    }

    private fun loadOrderDetails() {
        val orderId = args.orderId

        orderRepository.getOrderWithItemsById(orderId).observe(viewLifecycleOwner) { orderWithItems ->
            orderWithItems?.let {
                displayOrderDetails(it)
                loadOrderItems(orderId)
            }
        }
    }

    private fun loadOrderItems(orderId: Long) {
        orderRepository.getOrderItemsWithGamesByOrderId(orderId).observe(viewLifecycleOwner) { items ->
            orderItemsAdapter.submitList(items)
        }
    }

    private fun displayOrderDetails(orderWithItems: OrderWithItems) {
        val order = orderWithItems.order

        binding.orderIdTv.text = "Замовлення #${order.id}"

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        binding.orderDateTv.text = dateFormat.format(order.createdAt)

        binding.totalPriceTv.text = String.format("%.2f грн", order.totalPrice)
        binding.statusTv.text = getStatusText(order.status)
        binding.addressTv.text = order.deliveryAddress
        binding.phoneTv.text = order.phoneNumber
        binding.itemsCountTv.text = "${orderWithItems.items.size} товарів"
    }

    private fun getStatusText(status: String): String {
        return when (status) {
            "PENDING" -> "Очікує обробки"
            "CONFIRMED" -> "Підтверджено"
            "SHIPPED" -> "Відправлено"
            "DELIVERED" -> "Доставлено"
            "CANCELLED" -> "Скасовано"
            else -> status
        }
    }
}