package com.example.wort_des_tages_app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object Home : Screen("Home", Icons.Filled.Home)
    object Settings : Screen("Settings", Icons.Filled.Settings)
}