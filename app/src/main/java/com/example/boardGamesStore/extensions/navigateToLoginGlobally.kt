package com.example.boardGamesStore.extensions

import androidx.fragment.app.Fragment
import com.example.boardGamesStore.ui.main.MainActivity

fun Fragment.navigateToLoginGlobally() {
    (requireActivity() as? MainActivity)?.logoutAndNavigateToLogin()
}