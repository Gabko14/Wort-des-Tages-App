package com.example.wort_des_tages_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wort_des_tages_app.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Wort(
    var text: String?,
    var link: String?
)

data class WortdestagesState(
    var woerterDesTages: List<Wort>?
)

class WortDesTagesViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase = AppDatabase.getInstance(application.applicationContext)

    private val _state = MutableStateFlow(WortdestagesState(null))
    val state: StateFlow<WortdestagesState> get() = _state

    init {
        viewModelScope.launch {
            fetchWortDesTages()
        }
    }

    private suspend fun fetchWortDesTages() {
        var words = database.wortDao().getWortDesTages()
        if (words.isEmpty()) {
            database.wortDesTagesDao().createWortDesTages()
            words = database.wortDao().getWortDesTages()
        }

        _state.value = _state.value.copy(
            woerterDesTages = words.map { Wort(it.lemma, it.url) }
        )
    }
}
