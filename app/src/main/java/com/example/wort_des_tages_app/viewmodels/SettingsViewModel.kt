package com.example.wort_des_tages_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wort_des_tages_app.data.AppDatabase
import com.example.wort_des_tages_app.data.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    var anzahl_woerter: AnzahlWoerter? = null,
    var minFrequenzklasse: Int? = null,
    var amountSubstantiv: Int? = null,
    var amountAdjektiv: Int? = null,
    var amountVerb: Int? = null,
    var amountAdverb: Int? = null,
    var amountMehrwortausdruckOrNull: Int? = null,
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application.applicationContext)
    }

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> get() = _state

    init {
        viewModelScope.launch {
            fetchSettings()
        }
    }

    private suspend fun fetchSettings() {
        val settings = database.userSettingsDao().getUserSettings()
        if (settings != null) {
            _state.value = _state.value.copy(
                anzahl_woerter = settings.anzahl_woerter?.let { AnzahlWoerter.fromInt(it) },
                amountSubstantiv = settings.amountSubstantiv,
                amountAdjektiv = settings.amountAdjektiv,
                amountVerb = settings.amountVerb,
                amountAdverb = settings.amountAdverb,
                amountMehrwortausdruckOrNull = settings.amountMehrwortausdruckOrNull,
                minFrequenzklasse = settings.minFrequenzklasse
            )
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
                _state.value = _state.value.copy(
                    anzahl_woerter = newSettings.anzahl_woerter?.let { AnzahlWoerter.fromInt(it) },
                    amountSubstantiv = newSettings.amountSubstantiv,
                    amountAdjektiv = newSettings.amountAdjektiv,
                    amountVerb = newSettings.amountVerb,
                    amountAdverb = newSettings.amountAdverb,
                    amountMehrwortausdruckOrNull = newSettings.amountMehrwortausdruckOrNull,
                    minFrequenzklasse = newSettings.minFrequenzklasse
                )
            }
            database.wortDesTagesDao().createWortDesTages()
        }
    }
}