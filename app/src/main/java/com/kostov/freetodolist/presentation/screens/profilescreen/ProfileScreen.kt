package com.kostov.freetodolist.presentation.screens.profilescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: UserProfileViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val profileImage by viewModel.profileImageUrl.collectAsState()
    val darkModeState by viewModel.isDarkTheme.collectAsState()

    val usernameState by viewModel.userName.collectAsState()
    var username by remember { mutableStateOf("") }

    LaunchedEffect(usernameState) {
        username = usernameState
    }

    var darkMode by remember { mutableStateOf(darkModeState) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)

        AsyncImage(
            model = profileImage.ifBlank {
                "https://cataas.com/cat?width=200&height=200"
            },
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                val newCatUrl =
                    "https://cataas.com/cat?width=200&height=200&ts=${System.currentTimeMillis()}"
                viewModel.updateSettings(
                    settings.copy(profileUrl = newCatUrl)
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Generate my profile picture 😺")
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")
            Switch(checked = darkMode, onCheckedChange = { darkMode = it })
        }

        Button(
            onClick = {
                viewModel.updateSettings(
                    settings.copy(
                        username = username,
                        isDarkTheme = darkMode
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Settings")
        }
    }
}