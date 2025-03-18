package com.example.boardGamesStore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.database.AppDatabase
import com.example.boardGamesStore.data.repository.BoardGameRepository
import com.example.boardGamesStore.data.repository.OrderRepository
import com.example.boardGamesStore.data.repository.UserRepository
import com.example.boardGamesStore.databinding.FragmentAdminHomeBinding
import kotlinx.coroutines.launch

class AdminHomeFragment : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var boardGameRepository: BoardGameRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var userRepository: UserRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        boardGameRepository = BoardGameRepository(database.boardGameDao())
        orderRepository = OrderRepository(database.orderDao())
        userRepository = UserRepository(database.userDao())

        loadStatistics()
        setupClickListeners()
    }

    private fun loadStatistics() {
        viewLifecycleOwner.lifecycleScope.launch {
            boardGameRepository.allBoardGames.observe(viewLifecycleOwner) { games ->
                binding.totalProductsTv.text = games.size.toString()
            }

            orderRepository.getAllOrders().observe(viewLifecycleOwner) { orders ->
                val activeOrders = orders.count {
                    it.order.status in listOf("PENDING", "CONFIRMED", "SHIPPED")
                }
                binding.activeOrdersTv.text = activeOrders.toString()
            }

            userRepository.allUsers().observe(viewLifecycleOwner) { users ->
                binding.totalUsersTv.text = users.size.toString()
            }
        }
    }

    private fun setupClickListeners() {
        binding.addGameBtn.setOnClickListener {
            findNavController().navigate(R.id.addGameFragment)
        }

        binding.manageOrdersBtn.setOnClickListener {
            findNavController().navigate(R.id.adminOrdersFragment)
        }

        binding.manageGamesBtn.setOnClickListener {
            findNavController().navigate(R.id.adminGamesListFragment)
        }

        binding.profileImageBtn.setOnClickListener {
            findNavController().navigate(R.id.adminProfileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
