package com.example.wort_des_tages_app.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wort_des_tages_app.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = remember { listOf(Screen.Home, Screen.Settings) }

    TopAppBar(
        title = { Text("Wort des Tages") },
        modifier = modifier,
        actions = {
            screens.forEach { screen ->
                // Only show navigation buttons for screens we're not currently on
                if (currentRoute != screen.route) {
                    IconButton(
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination to avoid building up a stack
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when reselecting
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.route
                        )
                    }
                }
            }
        }
    )
}