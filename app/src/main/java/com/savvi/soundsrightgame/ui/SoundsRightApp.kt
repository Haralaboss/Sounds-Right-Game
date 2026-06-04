package com.savvi.soundsrightgame.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.savvi.soundsrightgame.ui.viewmodel.GameViewModel
import com.savvi.soundsrightgame.R
import com.savvi.soundsrightgame.ui.screens.*
import com.savvi.soundsrightgame.ui.theme.Cyan
import com.savvi.soundsrightgame.ui.theme.YellowText


@Composable
fun SoundsRightApp(gameViewModel: GameViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by gameViewModel.uiState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    // Logic to determine if we should show the exit dialog on back press
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    
    val gameRoutes = listOf("difficulty", "gameplay", "congratulations", "scoreboard", "timeout", "failed")
    val shouldShowExitDialog = currentRoute in gameRoutes

    Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        NavHost(navController = navController, startDestination = "menu") {
            composable("menu") {
                MenuScreen(
                    onPlayClicked = { navController.navigate("settings") },
                    onHelpClicked = { navController.navigate("help") }
                )
            }

            composable("help") {
                HelpScreen(
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = gameViewModel,
                    onBackClicked = { navController.popBackStack() },
                    onStartClicked = {
                        navController.navigate("difficulty") {
                            popUpTo("menu")
                        }
                    }
                )
            }

            composable("difficulty") {
                DifficultyScreen(
                    uiState = uiState,
                    onDifficultySelected = { difficulty ->
                        gameViewModel.selectDifficultyAndStartRound(difficulty)
                        navController.navigate("gameplay") {
                            popUpTo("difficulty") { inclusive = true }
                        }
                    }
                )
            }

            composable("gameplay") {
                GameplayScreen(
                    uiState = uiState,
                    onFoundClicked = {
                        gameViewModel.stopTimer()
                        navController.navigate("congratulations") {
                            popUpTo("gameplay") { inclusive = true }
                        }
                    },
                    onFailedClicked = {
                        gameViewModel.stopTimer()
                        navController.navigate("failed") {
                            popUpTo("gameplay") { inclusive = true }
                        }
                    },
                    onTimeOut = {
                        navController.navigate("timeout") {
                            popUpTo("gameplay") { inclusive = true }
                        }
                    }
                )
            }

            composable("timeout") {
                TimeOutScreen(
                    onContinueClicked = {
                        navController.navigate("scoreboard") {
                            popUpTo("timeout") { inclusive = true }
                        }
                    }
                )
            }

            composable("failed") {
                FailedScreen(
                    onContinueClicked = {
                        navController.navigate("scoreboard") {
                            popUpTo("failed") { inclusive = true }
                        }
                    }
                )
            }

            composable("congratulations") {
                CongratulationsScreen(
                    viewModel = gameViewModel,
                    onContinueClicked = {
                        navController.navigate("scoreboard") {
                            popUpTo("congratulations") { inclusive = true }
                        }
                    }
                )
            }

            composable("scoreboard") {
                ScoreboardScreen(
                    uiState = uiState,
                    onContinueClicked = {
                        gameViewModel.nextTurn()
                        if (gameViewModel.uiState.value.gameEnded) {
                            navController.navigate("endgame") {
                                popUpTo("scoreboard") { inclusive = true }
                            }
                        } else {
                            navController.navigate("difficulty") {
                                popUpTo("scoreboard") { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable("endgame") {
                EndGameScreen(
                    winners = gameViewModel.getWinners(),
                    uiState = uiState,
                    onMenuClicked = {
                        navController.navigate("menu") {
                            popUpTo("menu") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // BackHandler MUST be inside the same scope as the state it uses
        // and composed after NavHost to correctly intercept back presses
        BackHandler(enabled = shouldShowExitDialog) {
            showExitDialog = true
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = stringResource(R.string.exit_game_title),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            shadow = Shadow(
                                color = YellowText,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.exit_game_message),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            shadow = Shadow(
                                color = YellowText,
                                offset = Offset(1.5f, 1.5f),
                                blurRadius = 3f
                            )
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showExitDialog = false
                            navController.navigate("menu") {
                                popUpTo("menu") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan)
                    ) {
                        Text(
                            text = stringResource(R.string.end_game_button),
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
                },
                dismissButton = {
                    Button(
                        onClick = { showExitDialog = false },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan)
                    ) {
                        Text(
                            text = stringResource(R.string.stay_button),
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
    }
}
