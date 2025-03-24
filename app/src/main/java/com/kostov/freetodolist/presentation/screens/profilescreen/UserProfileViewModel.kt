package com.kostov.freetodolist.presentation.screens.profilescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kostov.freetodolist.data.datastore.UserSettingsDataStore
import com.kostov.freetodolist.data.local.usersettings.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings

    val userName: StateFlow<String> = settings.map { it.username }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        _settings.value.username
    )

    val profileImageUrl: StateFlow<String> = settings.map { it.profileUrl }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        _settings.value.profileUrl
    )

    val isDarkTheme: StateFlow<Boolean> = settings.map { it.isDarkTheme }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        _settings.value.isDarkTheme
    )

    init {
        viewModelScope.launch {
            UserSettingsDataStore.getSettings(application).collect {
                _settings.value = it
            }
        }
    }

    fun updateSettings(new: UserSettings) {
        _settings.value = new
        viewModelScope.launch {
            UserSettingsDataStore.saveSettings(getApplication(), new)
        }
    }
}