package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.repository.OrderRepository
import com.example.boardGamesStore.databinding.FragmentOrdersBinding
import com.example.boardGamesStore.domain.SessionManager
import com.example.boardGamesStore.ui.adapter.OrdersAdapter

class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var orderRepository: OrderRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderRepository = OrderRepository(AppDatabase.getDatabase(requireContext()).orderDao())
        sessionManager = SessionManager(requireContext())

        setupRecyclerView()
        loadOrders()

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_ordersFragment_to_profileFragment)
        }
    }

    // Додаємо обробник кліку для замовлення
    private fun setupRecyclerView() {
        ordersAdapter = OrdersAdapter { orderId ->
            // Обробка кліку на замовлення, наприклад, перехід до екрану деталей замовлення
            val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(orderId)
            findNavController().navigate(action)
        }
        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ordersAdapter
        }
    }


    private fun loadOrders() {
        val userId = sessionManager.getUserId()
        orderRepository.getOrdersForUser(userId).observe(viewLifecycleOwner) { orders ->
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
}