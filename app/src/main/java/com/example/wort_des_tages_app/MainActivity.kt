package com.example.wort_des_tages_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wort_des_tages_app.ui.TopBarNavigation
import com.example.wort_des_tages_app.ui.theme.WortdesTages_AppTheme
import com.example.wort_des_tages_app.viewmodels.SettingsViewModel
import com.example.wort_des_tages_app.views.Settings
import com.example.wort_des_tages_app.views.WortDesTages


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val settingsViewModel: SettingsViewModel = viewModel()
            val settingsState = settingsViewModel.state.collectAsState().value

            WortdesTages_AppTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopBarNavigation(navController, scrollBehavior)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) { WortDesTages(Modifier.padding(10.dp), settingsState.anzahlWoerter?.value) }
                        composable(Screen.Settings.route) { Settings(Modifier.padding(10.dp), settingsViewModel = settingsViewModel) }
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    val navController = rememberNavController()
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
//
//    val settingsViewModel: SettingsViewModel = viewModel()
//    val settingsState = settingsViewModel.state.collectAsState().value
//
//    WortdesTages_AppTheme {
//        Scaffold(
//            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//            topBar = {
//                TopBarNavigation(navController, scrollBehavior)
//            }
//        ) { innerPadding ->
//            NavHost(
//                navController,
//                startDestination = Screen.Home.route,
//                modifier = Modifier.padding(innerPadding)
//            ) {
//                composable(Screen.Home.route) { WortDesTages(Modifier.padding(10.dp), settingsState.anzahlWoerter.value) }
//                composable(Screen.Settings.route) { Settings(Modifier, settingsViewModel) }
//            }
//        }
//    }
//}