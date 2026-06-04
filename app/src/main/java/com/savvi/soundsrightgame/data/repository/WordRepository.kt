package com.savvi.soundsrightgame.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.savvi.soundsrightgame.data.model.WordDifficulty
import java.io.InputStreamReader
import java.util.Locale

class WordRepository(private val context: Context) {

    fun getRandomWord(difficulty: WordDifficulty): String {
        val languageCode = Locale.getDefault().language
        val fileName = if (languageCode == "el") "words_el.json" else "words_en.json"
        
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            val wordsMap: Map<String, List<String>> = Gson().fromJson(reader, type)
            
            val difficultyKey = when (difficulty) {
                WordDifficulty.EASY -> "easy"
                WordDifficulty.MEDIUM -> "medium"
                WordDifficulty.HARD -> "hard"
                WordDifficulty.IMPOSSIBLE -> "impossible"
            }
            
            val wordList = wordsMap[difficultyKey] ?: emptyList()
            if (wordList.isNotEmpty()) {
                wordList.random()
            } else {
                "Error: No words found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error reading words"
        }
    }
}
