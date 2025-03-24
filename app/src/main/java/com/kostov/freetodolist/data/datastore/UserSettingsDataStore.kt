package com.kostov.freetodolist.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kostov.freetodolist.data.local.usersettings.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserSettingsDataStore {
    private val Context.dataStore by preferencesDataStore("user_settings")

    private val USERNAME = stringPreferencesKey("username")
    private val PROFILE_URL = stringPreferencesKey("profile_url")
    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")

    suspend fun saveSettings(context: Context, settings: UserSettings) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME] = settings.username
            prefs[PROFILE_URL] = settings.profileUrl
            prefs[IS_DARK_THEME] = settings.isDarkTheme
        }
    }

    fun getSettings(context: Context): Flow<UserSettings> {
        return context.dataStore.data.map { prefs ->
            UserSettings(
                username = prefs[USERNAME] ?: "Your Name",
                profileUrl = prefs[PROFILE_URL] ?: "",
                isDarkTheme = prefs[IS_DARK_THEME] == true
            )
        }
    }
}