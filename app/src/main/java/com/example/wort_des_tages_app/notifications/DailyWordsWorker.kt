package com.example.wort_des_tages_app.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.wort_des_tages_app.data.AppDatabase
import com.example.wort_des_tages_app.viewmodels.Wort
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class DailyWordsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getInstance(applicationContext)
        val notificationHelper = NotificationHelper(applicationContext)
        
        try {
            // Fetch the user settings to know how many words to show
            val settings = database.userSettingsDao().getUserSettings()
            val maxWords = settings?.anzahl_woerter ?: 5
            
            // Make sure words are generated for today
            val words = database.wortDao().getWortDesTages()
            if (words.isEmpty()) {
                database.wortDesTagesDao().createWortDesTages()
            }
            
            // Fetch words again if they were empty
            val finalWords = if (words.isEmpty()) {
                database.wortDao().getWortDesTages()
            } else {
                words
            }
            
            // Convert database entities to view model objects
            val wordViewModels = finalWords
                .take(maxWords)
                .map { Wort(text = it.lemma, link = it.url) }
            
            // Show the notification
            notificationHelper.showWordOfDayNotification(wordViewModels)
            
            return Result.success()
        } catch (e: Exception) {
            // If there was an error, try again later
            return Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "daily_words_notification_worker"
        
        // Schedule the worker to run daily
        fun scheduleDaily(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
            
            // Calculate initial delay to target 8:00 AM
            val currentDateTime = LocalDateTime.now()
            val targetTime = LocalTime.of(8, 0) // 8:00 AM
            
            // If it's already past 8:00 AM, schedule for tomorrow
            var targetDateTime = LocalDateTime.of(
                currentDateTime.toLocalDate(),
                targetTime
            )
            
            if (currentDateTime.isAfter(targetDateTime)) {
                targetDateTime = targetDateTime.plusDays(1)
            }
            
            val initialDelay = currentDateTime.until(
                targetDateTime,
                java.time.temporal.ChronoUnit.MILLIS
            )
            
            val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyWordsWorker>(
                24, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                dailyWorkRequest
            )
        }
    }
} 