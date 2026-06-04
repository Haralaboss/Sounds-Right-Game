package com.savvi.soundsrightgame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.savvi.soundsrightgame.data.model.GameState
import com.savvi.soundsrightgame.data.model.WordDifficulty
import com.savvi.soundsrightgame.ui.theme.*

@Composable
fun DifficultyScreen(uiState: GameState, onDifficultySelected: (WordDifficulty) -> Unit) {
    val playerId = uiState.currentPlayer?.id ?: 1
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
        Text(
            text = stringResource(R.string.difficulty_title, playerId),
            style = TextStyle(fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        DifficultyButton(
            text = stringResource(R.string.difficulty_easy),
            color = EasyCard,
            onClick = { onDifficultySelected(WordDifficulty.EASY) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        DifficultyButton(
            text = stringResource(R.string.difficulty_medium),
            color = MediumCard,
            onClick = { onDifficultySelected(WordDifficulty.MEDIUM) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        DifficultyButton(
            text = stringResource(R.string.difficulty_hard),
            color = HardCard,
            onClick = { onDifficultySelected(WordDifficulty.HARD) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        DifficultyButton(
            text = stringResource(R.string.difficulty_impossible),
            color = ImpossibleCard,
            onClick = { onDifficultySelected(WordDifficulty.IMPOSSIBLE) }
        )
    }
}

@Composable
fun DifficultyButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(72.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
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
