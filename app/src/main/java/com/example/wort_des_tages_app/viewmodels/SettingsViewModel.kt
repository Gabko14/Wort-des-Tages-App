package com.example.wort_des_tages_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wort_des_tages_app.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AnzahlWoerter(val value: Int) {
    ONE(1), THREE(3), FIVE(5);

    companion object {
        fun fromInt(value: Int): AnzahlWoerter? {
            return entries.find { it.value == value }
        }
    }
}

data class SettingsState(
    var anzahlWoerter: AnzahlWoerter? = null
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
                anzahlWoerter = settings.anzahl_woerter?.let { AnzahlWoerter.fromInt(it) }
            )
        }
    }

    fun setAnzahlWoerter(anzahlWoerter: AnzahlWoerter?) {
//        if (anzahlWoerter == null) return
//        val result = database.userSettingsDao().setAnzahlWoerter(anzahlWoerter.value)
//        if (result.toInt() != 0) {
            _state.value = _state.value.copy(anzahlWoerter = anzahlWoerter)
//        }
    }
}
