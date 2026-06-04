package com.savvi.soundsrightgame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.savvi.soundsrightgame.R
import com.savvi.soundsrightgame.data.model.GameState
import com.savvi.soundsrightgame.data.model.WordDifficulty
import com.savvi.soundsrightgame.ui.components.GeneralButton
import com.savvi.soundsrightgame.ui.theme.*

@Composable
fun GameplayScreen(
    uiState: GameState,
    onFoundClicked: () -> Unit,
    onFailedClicked: () -> Unit,
    onTimeOut: () -> Unit
) {
    LaunchedEffect(uiState.timeLeftSeconds, uiState.isTimerRunning) {
        if (uiState.timeLeftSeconds == 0 && uiState.timeLimitSeconds != null && !uiState.isTimerRunning) {
            onTimeOut()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.round_indicator, uiState.currentRound),
            style = TextStyle(
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        if (uiState.timeLimitSeconds != null) {
            Text(
                text = "${uiState.timeLeftSeconds}",
                style = MaterialTheme.typography.displayLarge,
                color = if (uiState.timeLeftSeconds <= 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
            )
        } else {
            Text(
                text = "∞",
                style = MaterialTheme.typography.displayLarge
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        val difficultyColor = getDifficultyColor(uiState.currentDifficulty)
        val contentColor = Color.Black

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = difficultyColor,
                contentColor = contentColor
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.currentWord,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onFailedClicked,
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = RedButton),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "X",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.displayMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }

            Button(
                onClick = onFoundClicked,
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Cyan),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "✓",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.displayMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun FailedScreen(onContinueClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.failed_message),
            style = TextStyle(
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        GeneralButton(
            text= stringResource(R.string.continue_button),
            onClick = onContinueClicked)
    }
}

@Composable
fun TimeOutScreen(onContinueClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.time_out),
            style = TextStyle(
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        GeneralButton(
            text= stringResource(R.string.continue_button),
            onClick = onContinueClicked)
    }
}

fun getDifficultyColor(difficulty: WordDifficulty?): Color {
    return when (difficulty) {
        WordDifficulty.EASY -> EasyCard
        WordDifficulty.MEDIUM -> MediumCard
        WordDifficulty.HARD -> HardCard
        WordDifficulty.IMPOSSIBLE -> ImpossibleCard
        null -> Color.Gray
    }
}
