package com.github.pitman2e.mutespeaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.github.pitman2e.mutespeaker.ui.theme.MuteSpeakerTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppScreen() {
        val context = LocalContext.current
        val prefMgr = PreferenceManager.getDefaultSharedPreferences(context)

        var isEnabledChecked by remember {
            mutableStateOf(
                prefMgr.getBoolean(
                    context.getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE), false
                )
            )
        }

        var isPersistentChecked by remember {
            mutableStateOf(
                prefMgr.getBoolean(
                    context.getString(R.string.PREFERENCES_ID_DISABLE_NOTIFICATION_PERSIST), false
                )
            )
        }

        var sliderPosition by remember {
            mutableFloatStateOf(
                prefMgr.getInt(
                    context.getString(R.string.PREFERENCES_ID_DISABLE_SPEAKER_VOLUME),
                    20
                ).toFloat()
            )
        }

        MuteSpeakerTheme {
            Scaffold(topBar = {
                TopAppBar(colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), title = {
                    Text(text = stringResource(id = R.string.app_name))
                })
            }) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    SettingSwitch(
                        text = stringResource(id = R.string.PREFERENCES_TITLE_ENABLE_MUTE_SERVICE),
                        isChecked = isEnabledChecked,
                        onCheckedChange = { isChecked ->
                            isEnabledChecked = isChecked
                            if (isChecked) {
                                MuteServiceToggle.setEnable(context)
                            } else {
                                MuteServiceToggle.setDisable(context)
                            }
                        })
                    SettingSwitch(
                        text = stringResource(id = R.string.PREFERENCES_TITLE_DISABLE_NOTIFICATION_PERSIST),
                        isChecked = isPersistentChecked,
                        onCheckedChange = { isChecked ->
                            isPersistentChecked = isChecked
                            prefMgr.edit().putBoolean(
                                getString(R.string.PREFERENCES_ID_DISABLE_NOTIFICATION_PERSIST),
                                isChecked
                            ).apply()
                            MuteServiceToggle.enforceByPref(context)
                        })

                    EnableVolumeSlider(
                        text = stringResource(id = R.string.PREFERENCES_TITLE_DISABLE_SPEAKER_VOLUME),
                        sliderPosition = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                            prefMgr.edit().putInt(
                                getString(R.string.PREFERENCES_ID_DISABLE_SPEAKER_VOLUME),
                                it.roundToInt()
                            ).apply()
                        })
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun SettingSwitch(
        text: String,
        isChecked: Boolean,
        onCheckedChange: (isChecked: Boolean) -> Unit
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column() {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Switch(checked = isChecked, onCheckedChange = onCheckedChange)
        }
    }

    @Composable
    private fun EnableVolumeSlider(
        text: String,
        sliderPosition: Float,
        onValueChange: (value: Float) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = sliderPosition,
                onValueChange = onValueChange,
                steps = 100,
                valueRange = 0f..100f
            )
        }
    }

    @Composable
    @Preview
    fun AppScreenPreview() {
        AppScreen()
    }
}