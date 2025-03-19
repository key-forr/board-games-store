package com.example.boardGamesStore.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.boardGamesStore.R
import com.example.boardGamesStore.databinding.FragmentClientBinding

class ClientFragment : Fragment() {
    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding!!

    // Прапор для запобігання рекурсивних викликів
    private var isUpdatingNavigation = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.client_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Мапа зв'язків між призначеннями та пунктами меню
        val destinationToMenuItemMap = mapOf(
            R.id.homeFragment to R.id.homeFragment,
            R.id.listFragment to R.id.listFragment,
            R.id.cartFragment to R.id.cartFragment,
            R.id.checkoutFragment to R.id.cartFragment,
            R.id.profileFragment to R.id.profileFragment,
            R.id.ordersFragment to R.id.profileFragment
        )

        // Слухач змін дестинації
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (!isUpdatingNavigation) {
                isUpdatingNavigation = true

                try {
                    // Знаходимо відповідний пункт меню для поточного призначення
                    val menuItemId = destinationToMenuItemMap[destination.id]

                    // Встановлюємо вибраний пункт меню
                    if (menuItemId != null && binding.bottomNavigation.selectedItemId != menuItemId) {
                        binding.bottomNavigation.selectedItemId = menuItemId
                    }
                } finally {
                    isUpdatingNavigation = false
                }
            }
        }

        // Обробник натискань на меню
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (!isUpdatingNavigation) {
                isUpdatingNavigation = true

                try {
                    val currentDestId = navController.currentDestination?.id

                    // Перевіряємо, чи не знаходимося ми вже у відповідному фрагменті чи його дочірньому
                    val shouldNavigate = when (item.itemId) {
                        R.id.cartFragment -> currentDestId != R.id.cartFragment && currentDestId != R.id.checkoutFragment
                        R.id.profileFragment -> currentDestId != R.id.profileFragment && currentDestId != R.id.ordersFragment
                        else -> currentDestId != item.itemId
                    }

                    if (shouldNavigate) {
                        // Спочатку спробуємо навігацію до основного призначення
                        NavigationUI.onNavDestinationSelected(item, navController)
                    } else if (item.itemId == R.id.cartFragment && currentDestId == R.id.checkoutFragment) {
                        // Якщо ми в checkoutFragment і натиснули на cart - повертаємось назад
                        navController.popBackStack(R.id.cartFragment, false)
                    } else if (item.itemId == R.id.profileFragment && currentDestId == R.id.ordersFragment) {
                        // Якщо ми в ordersFragment і натиснули на profile - повертаємось назад
                        navController.popBackStack(R.id.profileFragment, false)
                    }

                    true
                } finally {
                    isUpdatingNavigation = false
                }
            } else {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}