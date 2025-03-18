package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.repository.OrderRepository
import com.example.boardGamesStore.databinding.FragmentAdminOrdersBinding
import com.example.boardGamesStore.ui.adapter.AdminOrdersAdapter
import kotlinx.coroutines.launch
import java.util.Date

class AdminOrdersFragment : Fragment() {
    private lateinit var binding: FragmentAdminOrdersBinding
    private lateinit var orderRepository: OrderRepository
    private lateinit var ordersAdapter: AdminOrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdminOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderRepository = OrderRepository(AppDatabase.getDatabase(requireContext()).orderDao())

        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        ordersAdapter = AdminOrdersAdapter { orderId, newStatus ->
            updateOrderStatus(orderId, newStatus)
        }

        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ordersAdapter
        }
    }

    private fun loadOrders() {
        orderRepository.getAllOrders().observe(viewLifecycleOwner) { orders ->
            if (orders.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.ordersRecyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.ordersRecyclerView.visibility = View.VISIBLE
                ordersAdapter.submitList(orders)
            }
        }
    }

    private fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            orderRepository.updateOrderStatus(orderId, newStatus)
        }
    }
}