package com.example.wort_des_tages_app.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (anzahlWoerter != null) {
            state.woerterDesTages?.take(anzahlWoerter)?.forEach { (text, link) ->
                LinkText(text, link, LocalContext.current)
            }
        }
    }
}