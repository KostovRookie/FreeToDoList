package com.kostov.freetodolist.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.kostov.freetodolist.navigation.MainScaffold
import com.kostov.freetodolist.presentation.screens.profilescreen.UserProfileViewModel
import com.kostov.freetodolist.ui.theme.FreeToDoListTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val profileViewModel: UserProfileViewModel = koinViewModel()
            val isDarkTheme = profileViewModel.settings.collectAsState()



            FreeToDoListTheme {
                //colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()

                val navController = rememberNavController()
                MainScaffold(navController = navController)
            }
        }
    }
}