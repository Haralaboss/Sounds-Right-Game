package com.savvi.soundsrightgame.data.repository

import android.content.Context
import androidx.core.content.edit

class PreferenceRepository(context: Context) {
    private val sharedPref = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    fun shouldShowRuleReminder(): Boolean {
        return !sharedPref.getBoolean("hide_rule_reminder", false)
    }

    fun setHideRuleReminder(hide: Boolean) {
        sharedPref.edit { putBoolean("hide_rule_reminder", hide) }
    }
}
