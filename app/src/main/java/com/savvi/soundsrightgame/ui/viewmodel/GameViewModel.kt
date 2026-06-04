package com.savvi.soundsrightgame.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.savvi.soundsrightgame.R
import com.savvi.soundsrightgame.data.model.*
import com.savvi.soundsrightgame.data.repository.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val wordRepository = WordRepository(application)
    private val preferenceRepository = PreferenceRepository(application)
    private var timerJob: Job? = null

    // --- Settings State ---
    var playersCountSetting by mutableStateOf(3f)
    var timeIndexSetting by mutableStateOf(1f)
    var roundsSetting by mutableStateOf(5f)
    var showRuleReminderDialog by mutableStateOf(false)
    var dontShowAgainSetting by mutableStateOf(false)

    val timeOptions = listOf(null, 20, 40, 60, 80)

    // --- Congratulations State ---
    val selectedPlayers = mutableStateListOf<Int>()
    var showNoSelectionWarning by mutableStateOf(false)

    fun togglePlayerSelection(playerId: Int) {
        if (selectedPlayers.contains(playerId)) {
            selectedPlayers.remove(playerId)
        } else {
            selectedPlayers.add(playerId)
        }
    }

    fun onContinueFromCongrats(onNavigate: () -> Unit) {
        if (selectedPlayers.isEmpty()) {
            showNoSelectionWarning = true
        } else {
            awardPoints(selectedPlayers.toList())
            selectedPlayers.clear()
            onNavigate()
        }
    }

    fun onStartGameClicked(onNavigate: () -> Unit) {
        if (preferenceRepository.shouldShowRuleReminder()) {
            showRuleReminderDialog = true
        } else {
            startGame()
            onNavigate()
        }
    }

    fun onConfirmRuleReminder(onNavigate: () -> Unit) {
        if (dontShowAgainSetting) {
            preferenceRepository.setHideRuleReminder(true)
        }
        showRuleReminderDialog = false
        startGame()
        onNavigate()
    }

    private fun startGame() {
        val count = playersCountSetting.toInt()
        val timeLimit = timeOptions[timeIndexSetting.toInt()]
        val rounds = roundsSetting.toInt()
        
        val players = (1..count).map { 
            Player(id = it, name = getApplication<Application>().getString(R.string.player_name, it)) 
        }
        _uiState.update {
            GameState(
                players = players,
                totalRounds = rounds,
                timeLimitSeconds = timeLimit,
                currentRound = 1,
                currentPlayerIndex = 0
            )
        }
    }

    fun selectDifficultyAndStartRound(difficulty: WordDifficulty) {
        val word = wordRepository.getRandomWord(difficulty)
        val timeLimit = _uiState.value.timeLimitSeconds
        
        _uiState.update {
            it.copy(
                currentDifficulty = difficulty,
                currentWord = word,
                timeLeftSeconds = timeLimit ?: 0,
                isTimerRunning = timeLimit != null
            )
        }
        
        if (timeLimit != null) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0 && _uiState.value.isTimerRunning) {
                delay(1000)
                _uiState.update { it.copy(timeLeftSeconds = it.timeLeftSeconds - 1) }
            }
            if (_uiState.value.timeLeftSeconds == 0) {
                _uiState.update { it.copy(isTimerRunning = false) }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    private fun awardPoints(foundByPlayerIds: List<Int>) {
        val difficulty = _uiState.value.currentDifficulty ?: return
        val pointsToAward = difficulty.points
        val currentPlayerId = _uiState.value.currentPlayer?.id

        _uiState.update { state ->
            val updatedPlayers = state.players.map { player ->
                if (foundByPlayerIds.contains(player.id) || player.id == currentPlayerId) {
                    player.copy(score = player.score + pointsToAward)
                } else {
                    player
                }
            }
            state.copy(players = updatedPlayers)
        }
    }

    fun nextTurn() {
        _uiState.update { state ->
            val nextPlayerIndex = state.currentPlayerIndex + 1
            if (nextPlayerIndex >= state.players.size) {
                // Round is over
                val nextRound = state.currentRound + 1
                if (nextRound > state.totalRounds) {
                    // Game is over
                    state.copy(gameEnded = true)
                } else {
                    // Start next round
                    state.copy(
                        currentPlayerIndex = 0,
                        currentRound = nextRound,
                        currentWord = "",
                        currentDifficulty = null
                    )
                }
            } else {
                // Next player in current round
                state.copy(
                    currentPlayerIndex = nextPlayerIndex,
                    currentWord = "",
                    currentDifficulty = null
                )
            }
        }
    }

    fun getWinners(): List<Player> {
        val maxScore = _uiState.value.players.maxOfOrNull { it.score } ?: 0
        return _uiState.value.players.filter { it.score == maxScore }
    }
}
