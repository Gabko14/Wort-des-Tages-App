package com.example.wort_des_tages_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wort_des_tages_app.ui.TopBarNavigation
import com.example.wort_des_tages_app.ui.theme.WortdesTages_AppTheme
import com.example.wort_des_tages_app.viewmodels.SettingsViewModel
import com.example.wort_des_tages_app.views.Settings
import com.example.wort_des_tages_app.views.WortDesTages


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val settingsViewModel: SettingsViewModel = viewModel()
            val settingsState = settingsViewModel.state.collectAsState().value

            WortdesTages_AppTheme {
                Scaffold(
                    topBar = {
                        TopBarNavigation(navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) { WortDesTages(Modifier.padding(16.dp).padding(top = 20.dp), settingsState.anzahl_woerter?.value) }
                        composable(Screen.Settings.route) { Settings(settingsViewModel = settingsViewModel, Modifier.padding(16.dp).padding(top = 32.dp)) }
                    }
                }
            }
        }
    }
}