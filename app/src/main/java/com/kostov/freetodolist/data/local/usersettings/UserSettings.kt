package com.kostov.freetodolist.data.local.usersettings

data class UserSettings(
    val username: String = "Your Name",
    val profileUrl: String = "",
    val isDarkTheme: Boolean = false
)