package com.example.wort_des_tages_app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wort_des_tages_app.MainActivity
import com.example.wort_des_tages_app.R
import com.example.wort_des_tages_app.data.Wort
import com.example.wort_des_tages_app.viewmodels.Wort as WortViewModel

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "wort_des_tages_channel"
        const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Wort des Tages"
            val descriptionText = "Daily German word notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showWordOfDayNotification(words: List<WortViewModel>) {
        if (words.isEmpty()) return

        // Create an intent that opens the app when the notification is tapped
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, 
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification content from words
        val contentTitle = "Wort des Tages"
        val contentText = buildNotificationContent(words)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a more appropriate icon if available
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(NOTIFICATION_ID, builder.build())
            } catch (e: SecurityException) {
                // Handle the case where notification permission is not granted
                // This is typically handled in a real app with a permission request
            }
        }
    }

    private fun buildNotificationContent(words: List<WortViewModel>): String {
        return if (words.isEmpty()) {
            "Check out today's German words!"
        } else {
            val wordsText = words.mapNotNull { it.text }
                .filter { it.isNotEmpty() }
                .joinToString(", ")
            
            if (wordsText.isEmpty()) {
                "Check out today's German words!"
            } else {
                "Today's words: $wordsText"
            }
        }
    }
} 