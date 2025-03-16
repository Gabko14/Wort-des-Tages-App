package com.example.wort_des_tages_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.wort_des_tages_app.data.AppDatabase
import com.example.wort_des_tages_app.data.UserSettings
import com.example.wort_des_tages_app.notifications.DailyWordsWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AnzahlWoerter(val value: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    companion object {
        fun fromInt(value: Int): AnzahlWoerter? {
            return entries.find { it.value == value }
        }
    }
}

data class SettingsState(
    val isLoading: Boolean = false,
    val anzahl_woerter: AnzahlWoerter? = null,
    val minFrequenzklasse: Int? = null,
    val amountSubstantiv: Int? = null,
    val amountAdjektiv: Int? = null,
    val amountVerb: Int? = null,
    val amountAdverb: Int? = null,
    val amountMehrwortausdruckOrNull: Int? = null,
    val notificationsEnabled: Boolean = true,
    val error: String? = null
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application.applicationContext)
    }
    
    private val workManager = WorkManager.getInstance(application)

    private val _state = MutableStateFlow(SettingsState(isLoading = true))
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            fetchSettings()
        }
    }

    private suspend fun fetchSettings() {
        try {
            _state.update { it.copy(isLoading = true, error = null) }

            val settings = database.userSettingsDao().getUserSettings()

            if (settings != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        anzahl_woerter = settings.anzahl_woerter?.let { value ->
                            AnzahlWoerter.fromInt(
                                value
                            )
                        },
                        amountSubstantiv = settings.amountSubstantiv,
                        amountAdjektiv = settings.amountAdjektiv,
                        amountVerb = settings.amountVerb,
                        amountAdverb = settings.amountAdverb,
                        amountMehrwortausdruckOrNull = settings.amountMehrwortausdruckOrNull,
                        minFrequenzklasse = settings.minFrequenzklasse,
                        notificationsEnabled = settings.notificationsEnabled
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error fetching settings"
                )
            }
        }
    }

    fun updateSettings(
        anzahl_woerter: Int? = null,
        amountSubstantiv: Int? = null,
        amountAdjektiv: Int? = null,
        amountVerb: Int? = null,
        amountAdverb: Int? = null,
        amountMehrwortausdruckOrNull: Int? = null,
        minFrequenzklasse: Int? = null,
        notificationsEnabled: Boolean? = null
    ) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                // Get current state values to use as defaults
                val currentState = _state.value
                
                val newSettings = UserSettings(
                    id = 1,
                    anzahl_woerter = anzahl_woerter ?: currentState.anzahl_woerter?.value ?: 0,
                    amountSubstantiv = amountSubstantiv ?: currentState.amountSubstantiv ?: 0,
                    amountAdjektiv = amountAdjektiv ?: currentState.amountAdjektiv ?: 0,
                    amountVerb = amountVerb ?: currentState.amountVerb ?: 0,
                    amountAdverb = amountAdverb ?: currentState.amountAdverb ?: 0,
                    amountMehrwortausdruckOrNull = amountMehrwortausdruckOrNull ?: currentState.amountMehrwortausdruckOrNull ?: 0,
                    minFrequenzklasse = minFrequenzklasse ?: currentState.minFrequenzklasse ?: 0,
                    notificationsEnabled = notificationsEnabled ?: currentState.notificationsEnabled
                )

                val resultSettings = database.userSettingsDao().updateUserSettings(newSettings)

                if (resultSettings != 0) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            anzahl_woerter = newSettings.anzahl_woerter?.let { value ->
                                AnzahlWoerter.fromInt(
                                    value
                                )
                            },
                            amountSubstantiv = newSettings.amountSubstantiv,
                            amountAdjektiv = newSettings.amountAdjektiv,
                            amountVerb = newSettings.amountVerb,
                            amountAdverb = newSettings.amountAdverb,
                            amountMehrwortausdruckOrNull = newSettings.amountMehrwortausdruckOrNull,
                            minFrequenzklasse = newSettings.minFrequenzklasse,
                            notificationsEnabled = newSettings.notificationsEnabled
                        )
                    }
                    
                    // Handle notification scheduling based on enabled/disabled setting
                    if (newSettings.notificationsEnabled) {
                        // Schedule daily notifications if they're enabled
                        DailyWordsWorker.scheduleDaily(getApplication())
                    } else {
                        // Cancel scheduled notifications if they're disabled
                        workManager.cancelUniqueWork("daily_words_notification_worker")
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to save settings"
                        )
                    }
                }

                // Generate new words for the day based on updated settings
                database.wortDesTagesDao().createWortDesTages()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Error updating settings"
                    )
                }
            }
        }
    }
    
    fun toggleNotifications() {
        val currentNotificationsEnabled = _state.value.notificationsEnabled
        updateSettings(notificationsEnabled = !currentNotificationsEnabled)
    }
    
    fun testNotification() {
        viewModelScope.launch {
            try {
                val database = AppDatabase.getInstance(getApplication())
                
                // Fetch current words of the day
                var words = database.wortDao().getWortDesTages()
                
                // If no words are available, generate them
                if (words.isEmpty()) {
                    database.wortDesTagesDao().createWortDesTages()
                    words = database.wortDao().getWortDesTages()
                }
                
                // Get the number of words to show from settings
                val settings = _state.value
                val wordCount = settings.anzahl_woerter?.value ?: 5
                
                // Convert to view model objects
                val wordViewModels = words
                    .take(wordCount)
                    .map { com.example.wort_des_tages_app.viewmodels.Wort(text = it.lemma, link = it.url) }
                
                // Show the notification
                val notificationHelper = com.example.wort_des_tages_app.notifications.NotificationHelper(getApplication())
                notificationHelper.showWordOfDayNotification(wordViewModels)
            } catch (e: Exception) {
                // Ignore any errors during test notification
            }
        }
    }
}