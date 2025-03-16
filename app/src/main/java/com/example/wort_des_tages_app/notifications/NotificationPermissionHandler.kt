package com.example.wort_des_tages_app.notifications

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.Manifest

object NotificationPermissionHandler {
    
    /**
     * Checks if notification permission is granted.
     * For Android 13+ (API 33+), checks the notification permission.
     * For older versions, always returns true since the permission is granted at install time.
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Permission is granted at install time for API level < 33
            true
        }
    }
    
    /**
     * Sets up notification permission request in an activity.
     * Call this in your activity's onCreate method.
     */
    fun setupNotificationPermissionRequest(activity: ComponentActivity, onPermissionResult: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                onPermissionResult(isGranted)
            }
            
            // Check and request permission if needed
            if (!hasNotificationPermission(activity)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onPermissionResult(true)
            }
        } else {
            // Permission is already granted for older API levels
            onPermissionResult(true)
        }
    }
} 