package com.example.wort_des_tages_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wort_des_tages_app.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Wort(
    val text: String? = null,
    val link: String? = null
)

data class WortdestagesState(
    val isLoading: Boolean = false,
    val woerterDesTages: List<Wort> = emptyList(),
    val error: String? = null
)

class WortDesTagesViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase = AppDatabase.getInstance(application.applicationContext)

    private val _state = MutableStateFlow(WortdestagesState(isLoading = true))
    val state: StateFlow<WortdestagesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            fetchWortDesTages()
        }
    }

    private suspend fun fetchWortDesTages() {
        try {
            _state.update { it.copy(isLoading = true, error = null) }
            
            var words = database.wortDao().getWortDesTages()
            
            if (words.isEmpty()) {
                database.wortDesTagesDao().createWortDesTages()
                words = database.wortDao().getWortDesTages()
            }

            _state.update { 
                it.copy(
                    isLoading = false,
                    woerterDesTages = words.map { word -> 
                        Wort(text = word.lemma, link = word.url) 
                    }
                )
            }
        } catch (e: Exception) {
            _state.update { 
                it.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error fetching words"
                )
            }
        }
    }
}
