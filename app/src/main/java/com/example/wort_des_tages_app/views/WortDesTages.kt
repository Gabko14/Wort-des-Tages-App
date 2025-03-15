package com.example.wort_des_tages_app.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wort_des_tages_app.viewmodels.WortDesTagesViewModel

@Composable
fun WortDesTages(
    modifier: Modifier = Modifier,
    anzahlWoerter: Int?,
    viewModel: WortDesTagesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val displayWords = state.woerterDesTages.let { words ->
        anzahlWoerter?.let { limit ->
            words.take(limit)
        } ?: words
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(
            items = displayWords,
            key = { word -> word.text ?: "" }  // Use a stable key for better performance
        ) { word ->
            LinkText(
                text = word.text,
                url = word.link,
                context = context
            )
        }
    }
}