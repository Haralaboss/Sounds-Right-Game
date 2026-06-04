package com.savvi.soundsrightgame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.savvi.soundsrightgame.R
import com.savvi.soundsrightgame.ui.viewmodel.GameViewModel
import com.savvi.soundsrightgame.ui.components.GeneralButton
import com.savvi.soundsrightgame.ui.theme.YellowText
import com.savvi.soundsrightgame.ui.theme.Cyan

@Composable
fun SettingsScreen(
    viewModel: GameViewModel,
    onBackClicked: () -> Unit,
    onStartClicked: () -> Unit
) {
    val timeOptions = viewModel.timeOptions
    val timeText = if (timeOptions[viewModel.timeIndexSetting.toInt()] == null) 
        stringResource(R.string.settings_untimed) 
    else 
        "${timeOptions[viewModel.timeIndexSetting.toInt()]} s"

    if (viewModel.showRuleReminderDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showRuleReminderDialog = false },
            title = { Text(stringResource(R.string.rule_reminder_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.rule_reminder_message))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = viewModel.dontShowAgainSetting, 
                            onCheckedChange = { viewModel.dontShowAgainSetting = it }
                        )
                        Text(stringResource(R.string.dont_show_again))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.onConfirmRuleReminder(onStartClicked)
                }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.settings_title),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displaySmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = YellowText,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                "${stringResource(R.string.settings_players)}: ${viewModel.playersCountSetting.toInt()}",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = YellowText,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
            Slider(
                value = viewModel.playersCountSetting,
                onValueChange = { viewModel.playersCountSetting = it },
                valueRange = 2f..8f,
                steps = 5,
                colors = SliderDefaults.colors(YellowText, Cyan)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "${stringResource(R.string.settings_time)}: $timeText",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = YellowText,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
            Slider(
                value = viewModel.timeIndexSetting,
                onValueChange = { viewModel.timeIndexSetting = it },
                valueRange = 0f..4f,
                steps = 3,
                colors = SliderDefaults.colors(YellowText, Cyan)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "${stringResource(R.string.settings_rounds)}: ${viewModel.roundsSetting.toInt()}",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = YellowText,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
            Slider(
                value = viewModel.roundsSetting,
                onValueChange = { viewModel.roundsSetting = it },
                valueRange = 1f..20f,
                steps = 18,
                colors = SliderDefaults.colors(YellowText, Cyan)
            )

            Spacer(modifier = Modifier.height(32.dp))

            GeneralButton(
                text = stringResource(R.string.start),
                onClick = {
                    viewModel.onStartGameClicked(onStartClicked)
                })
        }

        if (viewModel.playersCountSetting.toInt() == 2) {
            Text(
                text = stringResource(R.string.settings_warning),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = YellowText,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}
