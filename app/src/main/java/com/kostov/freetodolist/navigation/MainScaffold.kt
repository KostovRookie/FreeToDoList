package com.kostov.freetodolist.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScaffold(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("main", Icons.Default.Face, "Tasks"),
        BottomNavItem("kitten", Icons.Default.Favorite, "Kitten")
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute ?: "",
                onFabClick = { navController.navigate("add_task") }
            )
        }
    ) { padding ->
        ToDoNavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}