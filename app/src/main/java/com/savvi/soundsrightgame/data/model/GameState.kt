package com.savvi.soundsrightgame.data.model

data class GameState(
    val players: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val currentRound: Int = 1,
    val totalRounds: Int = 5,
    val timeLimitSeconds: Int? = null, // null means untimed
    val currentWord: String = "",
    val currentDifficulty: WordDifficulty? = null,
    val timeLeftSeconds: Int = 0,
    val isTimerRunning: Boolean = false,
    val gameEnded: Boolean = false
) {
    val currentPlayer: Player?
        get() = players.getOrNull(currentPlayerIndex)
}
