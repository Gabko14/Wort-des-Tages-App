package com.example.wort_des_tages_app.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wort_des_tages_app.viewmodels.SettingsViewModel
import kotlin.math.roundToInt

@Composable
fun Settings(settingsViewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val settingsState by settingsViewModel.state.collectAsState()
    val context = LocalContext.current

    var anzahlWoerter by remember { mutableFloatStateOf(settingsState.anzahl_woerter?.value?.toFloat() ?: 0f) }
    var amountSubstantiv by remember { mutableFloatStateOf(settingsState.amountSubstantiv?.toFloat() ?: 0f) }
    var amountAdjektiv by remember { mutableFloatStateOf(settingsState.amountAdjektiv?.toFloat() ?: 0f) }
    var amountVerb by remember { mutableFloatStateOf(settingsState.amountVerb?.toFloat() ?: 0f) }
    var amountAdverb by remember { mutableFloatStateOf(settingsState.amountAdverb?.toFloat() ?: 0f) }
    var amountMehrwortausdruckOrNull by remember { mutableFloatStateOf(settingsState.amountMehrwortausdruckOrNull?.toFloat() ?: 0f) }
    var minFrequenzklasse by remember { mutableFloatStateOf(settingsState.minFrequenzklasse?.toFloat() ?: 0f) }
    LazyColumn(
        modifier = modifier.padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            SettingItem(
                "Anzahl Wörter",
                value = anzahlWoerter,
                onValueChange = {
                    anzahlWoerter = it.roundToInt().toFloat()
                },
                min = 1f,
            )
        }
        item {
            SettingItem(
                "Anzahl Substantiv",
                value = amountSubstantiv,
                onValueChange = {
                    amountSubstantiv = it.roundToInt().toFloat()
                }
            )
        }
        item {
            SettingItem(
                "Anzahl Adjektiv",
                value = amountAdjektiv,
                onValueChange = {
                    amountAdjektiv = it.roundToInt().toFloat()
                }
            )
        }
        item {
            SettingItem(
                "Anzahl Verb",
                value = amountVerb,
                onValueChange = {
                    amountVerb = it.roundToInt().toFloat()
                }
            )
        }
        item {
            SettingItem(
                "Anzahl Adverb",
                value = amountAdverb,
                onValueChange = {
                    amountAdverb = it.roundToInt().toFloat()
                }
            )
        }
        item {
            SettingItem(
                "Anzahl Mehrwortausdruck (Oder unbekannt)",
                value = amountMehrwortausdruckOrNull,
                onValueChange = {
                    amountMehrwortausdruckOrNull = it.roundToInt().toFloat()
                }
            )
        }
        item {
            SettingItem(
                "Min Frequenzklasse",
                value = minFrequenzklasse,
                onValueChange = {
                    minFrequenzklasse = it.roundToInt().toFloat()
                }
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp, end = 25.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(onClick = {
            settingsViewModel.updateSettings(
                anzahl_woerter = anzahlWoerter.roundToInt(),
                amountSubstantiv = amountSubstantiv.roundToInt(),
                amountAdjektiv = amountAdjektiv.roundToInt(),
                amountVerb = amountVerb.roundToInt(),
                amountAdverb = amountAdverb.roundToInt(),
                amountMehrwortausdruckOrNull = amountMehrwortausdruckOrNull.roundToInt(),
                minFrequenzklasse = minFrequenzklasse.roundToInt()
            )
            Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
        }) {
            Text("Save")
        }
    }
}

@Composable
fun SettingItem(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float = 0f,
    max: Float = 5f
) {
    Text(text = label)
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = min..max
    )
    Text(text = value.toString())
}