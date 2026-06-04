package com.savvi.soundsrightgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.savvi.soundsrightgame.R
import com.savvi.soundsrightgame.ui.viewmodel.GameViewModel
import com.savvi.soundsrightgame.data.model.GameState
import com.savvi.soundsrightgame.data.model.Player
import com.savvi.soundsrightgame.ui.components.GeneralButton
import com.savvi.soundsrightgame.ui.theme.Cyan
import com.savvi.soundsrightgame.ui.theme.YellowText

@Composable
fun CongratulationsScreen(
    viewModel: GameViewModel,
    onContinueClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentPlayerId = uiState.currentPlayer?.id

    if (viewModel.showNoSelectionWarning) {
        AlertDialog(
            onDismissRequest = { viewModel.showNoSelectionWarning = false },
            title = { Text(
                text = stringResource(R.string.no_selection_title),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    shadow = Shadow(
                        color = YellowText,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            ) },
            text = { Text(
                text = stringResource(R.string.no_selection_message),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,) },
            confirmButton = {
                Button(
                    onClick = { viewModel.showNoSelectionWarning = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cyan)
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = YellowText,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.congrats_title),
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.players.filter { it.id != currentPlayerId }) { player ->
                val isSelected = viewModel.selectedPlayers.contains(player.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.togglePlayerSelection(player.id)
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = null // The Row handles the click logic
                    )
                    Text(
                        text = player.name,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 8.dp,top = 8.dp, end = 0.dp, bottom = 8.dp)
                    )
                }
            }
        }

        GeneralButton(
            text = stringResource(R.string.continue_button),
            onClick = {
                viewModel.onContinueFromCongrats(onContinueClicked)
            }
        )
    }
}

@Composable
fun ScoreboardScreen(
    uiState: GameState,
    onContinueClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.round_indicator,uiState.currentRound),
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.scoreboard_title),
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.players.sortedByDescending { it.score }) { player ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(player.name,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 8.dp,top = 8.dp, end = 0.dp, bottom = 8.dp))

                    Text("${player.score}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 8.dp,top = 8.dp, end = 0.dp, bottom = 8.dp))
                }
                HorizontalDivider()
            }
        }

        GeneralButton(
            text = stringResource(R.string.continue_button),
            onClick = onContinueClicked
        )
    }
}

@Composable
fun EndGameScreen(
    winners: List<Player>,
    uiState: GameState,
    onMenuClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val title = if (winners.size == 1) {
            stringResource(R.string.player_won, winners.first().id)
        } else {
            stringResource(R.string.tie_game)
        }

        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.scoreboard_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.players.sortedByDescending { it.score }) { player ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(player.name, style = MaterialTheme.typography.titleMedium)
                    Text("${player.score}", style = MaterialTheme.typography.titleMedium)
                }
                HorizontalDivider()
            }
        }

        Button(
            onClick = onMenuClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.menu_button))
        }
    }
}
