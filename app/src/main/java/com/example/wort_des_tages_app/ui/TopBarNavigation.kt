package com.example.wort_des_tages_app.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wort_des_tages_app.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val screens = listOf(Screen.Home, Screen.Settings)

    TopAppBar(
        title = { Text("Wort des Tages") },
        actions = {
            screens.forEach { route ->
                if (currentRoute != route.route) {
                    IconButton(onClick = { navController.navigate(route.route) }) {
                        Icon(
                            route.icon,
                            contentDescription = route.route
                        )
                    }
                }
            }
        },
    )
}