package com.example.wort_des_tages_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wort_des_tages_app.notifications.DailyWordsWorker
import com.example.wort_des_tages_app.notifications.NotificationPermissionHandler
import com.example.wort_des_tages_app.ui.TopBarNavigation
import com.example.wort_des_tages_app.ui.theme.WortdesTages_AppTheme
import com.example.wort_des_tages_app.viewmodels.SettingsViewModel
import com.example.wort_des_tages_app.viewmodels.WortDesTagesViewModel
import com.example.wort_des_tages_app.views.Settings
import com.example.wort_des_tages_app.views.WortDesTages

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Request notification permission and schedule daily notifications
        NotificationPermissionHandler.setupNotificationPermissionRequest(this) { isGranted ->
            if (isGranted) {
                // Schedule daily notifications if permission is granted
                DailyWordsWorker.scheduleDaily(applicationContext)
            } else {
                // Inform the user that they will not receive notifications
                Toast.makeText(
                    this,
                    "Notification permission denied. You won't receive daily word notifications.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        
        setContent {
            WortdesTages_AppTheme {
                WortDesTagesApp()
            }
        }
    }
}

@Composable
fun WortDesTagesApp(
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel = viewModel(),
    wortDesTagesViewModel: WortDesTagesViewModel = viewModel()
) {
    val settingsState by settingsViewModel.state.collectAsState()
    
    val contentPadding = remember { Modifier.padding(horizontal = 16.dp, vertical = 20.dp) }
    
    Scaffold(
        topBar = {
            TopBarNavigation(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                WortDesTages(
                    modifier = contentPadding,
                    anzahlWoerter = settingsState.anzahl_woerter?.value,
                    viewModel = wortDesTagesViewModel
                )
            }
            composable(Screen.Settings.route) { 
                Settings(
                    settingsViewModel = settingsViewModel,
                    modifier = contentPadding
                )
            }
        }
    }
}