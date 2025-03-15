package com.example.wort_des_tages_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wort_des_tages_app.data.AppDatabase
import com.example.wort_des_tages_app.data.UserSettings
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
    val error: String? = null
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application.applicationContext)
    }

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
                        minFrequenzklasse = settings.minFrequenzklasse
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
        minFrequenzklasse: Int? = null
    ) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val newSettings = UserSettings(
                    id = 1,
                    anzahl_woerter = anzahl_woerter ?: 0,
                    amountSubstantiv = amountSubstantiv ?: 0,
                    amountAdjektiv = amountAdjektiv ?: 0,
                    amountVerb = amountVerb ?: 0,
                    amountAdverb = amountAdverb ?: 0,
                    amountMehrwortausdruckOrNull = amountMehrwortausdruckOrNull ?: 0,
                    minFrequenzklasse = minFrequenzklasse ?: 0
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
                            minFrequenzklasse = newSettings.minFrequenzklasse
                        )
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
}