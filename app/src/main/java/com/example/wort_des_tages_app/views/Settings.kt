package com.example.wort_des_tages_app.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wort_des_tages_app.viewmodels.SettingsViewModel
import kotlin.math.roundToInt

data class SettingsUiState(
    val anzahlWoerter: Float = 0f,
    val amountSubstantiv: Float = 0f,
    val amountAdjektiv: Float = 0f,
    val amountVerb: Float = 0f,
    val amountAdverb: Float = 0f,
    val amountMehrwortausdruckOrNull: Float = 0f,
    val minFrequenzklasse: Float = 0f,
    val notificationsEnabled: Boolean = true
)

@Composable
fun Settings(settingsViewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val viewModelState by settingsViewModel.state.collectAsState()
    val context = LocalContext.current

    var uiState by remember(viewModelState) {
        mutableStateOf(
            SettingsUiState(
                anzahlWoerter = viewModelState.anzahl_woerter?.value?.toFloat() ?: 0f,
                amountSubstantiv = viewModelState.amountSubstantiv?.toFloat() ?: 0f,
                amountAdjektiv = viewModelState.amountAdjektiv?.toFloat() ?: 0f,
                amountVerb = viewModelState.amountVerb?.toFloat() ?: 0f,
                amountAdverb = viewModelState.amountAdverb?.toFloat() ?: 0f,
                amountMehrwortausdruckOrNull = viewModelState.amountMehrwortausdruckOrNull?.toFloat()
                    ?: 0f,
                minFrequenzklasse = viewModelState.minFrequenzklasse?.toFloat() ?: 0f,
                notificationsEnabled = viewModelState.notificationsEnabled
            )
        )
    }

    val hasChanges by remember(uiState, viewModelState) {
        derivedStateOf {
            uiState.anzahlWoerter.roundToInt() != viewModelState.anzahl_woerter?.value ||
                    uiState.amountSubstantiv.roundToInt() != viewModelState.amountSubstantiv ||
                    uiState.amountAdjektiv.roundToInt() != viewModelState.amountAdjektiv ||
                    uiState.amountVerb.roundToInt() != viewModelState.amountVerb ||
                    uiState.amountAdverb.roundToInt() != viewModelState.amountAdverb ||
                    uiState.amountMehrwortausdruckOrNull.roundToInt() != viewModelState.amountMehrwortausdruckOrNull ||
                    uiState.minFrequenzklasse.roundToInt() != viewModelState.minFrequenzklasse ||
                    uiState.notificationsEnabled != viewModelState.notificationsEnabled
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = modifier.padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Notification Settings Section
            item {
                Text(
                    text = "Notification Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Daily Word Notifications",
                        modifier = Modifier.weight(1f)
                    )
                    
                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { isEnabled ->
                            uiState = uiState.copy(notificationsEnabled = isEnabled)
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Receive a daily notification with your words of the day",
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
                
                if (uiState.notificationsEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                settingsViewModel.testNotification()
                                Toast.makeText(
                                    context, 
                                    "Test notification sent", 
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Text("Test Notification")
                        }
                    }
                }
                
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            
            // Word Count Settings Section
            item {
                Text(
                    text = "Word Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                SettingSlider(
                    label = "Anzahl Wörter",
                    value = uiState.anzahlWoerter,
                    onValueChange = {
                        uiState = uiState.copy(anzahlWoerter = it.roundToInt().toFloat())
                    },
                    min = 1f,
                    max = 5f
                )
            }

            item {
                SettingSlider(
                    label = "Anzahl Substantiv",
                    value = uiState.amountSubstantiv,
                    onValueChange = {
                        uiState = uiState.copy(amountSubstantiv = it.roundToInt().toFloat())
                    },
                    max = 5f
                )
            }

            item {
                SettingSlider(
                    label = "Anzahl Adjektiv",
                    value = uiState.amountAdjektiv,
                    onValueChange = {
                        uiState = uiState.copy(amountAdjektiv = it.roundToInt().toFloat())
                    },
                    max = 5f
                )
            }

            item {
                SettingSlider(
                    label = "Anzahl Verb",
                    value = uiState.amountVerb,
                    onValueChange = {
                        uiState = uiState.copy(amountVerb = it.roundToInt().toFloat())
                    },
                    max = 5f
                )
            }

            item {
                SettingSlider(
                    label = "Anzahl Adverb",
                    value = uiState.amountAdverb,
                    onValueChange = {
                        uiState = uiState.copy(amountAdverb = it.roundToInt().toFloat())
                    },
                    max = 5f
                )
            }

            item {
                SettingSlider(
                    label = "Anzahl Mehrwortausdruck (Oder unbekannt)",
                    value = uiState.amountMehrwortausdruckOrNull,
                    onValueChange = {
                        uiState =
                            uiState.copy(amountMehrwortausdruckOrNull = it.roundToInt().toFloat())
                    },
                    max = 5f
                )
            }

            item {
                SettingSlider(
                    label = "Min Frequenzklasse",
                    value = uiState.minFrequenzklasse,
                    onValueChange = {
                        uiState = uiState.copy(minFrequenzklasse = it.roundToInt().toFloat())
                    },
                    max = 5f
                )
            }
        }

        Button(
            onClick = {
                settingsViewModel.updateSettings(
                    anzahl_woerter = uiState.anzahlWoerter.roundToInt(),
                    amountSubstantiv = uiState.amountSubstantiv.roundToInt(),
                    amountAdjektiv = uiState.amountAdjektiv.roundToInt(),
                    amountVerb = uiState.amountVerb.roundToInt(),
                    amountAdverb = uiState.amountAdverb.roundToInt(),
                    amountMehrwortausdruckOrNull = uiState.amountMehrwortausdruckOrNull.roundToInt(),
                    minFrequenzklasse = uiState.minFrequenzklasse.roundToInt(),
                    notificationsEnabled = uiState.notificationsEnabled
                )
                Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 25.dp),
            enabled = hasChanges
        ) {
            Text("Save")
        }
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float = 0f,
    max: Float = 5f
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = min..max,
                steps = (max - min).toInt() - 1,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = value.roundToInt().toString(),
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}