package com.kostov.freetodolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kostov.freetodolist.presentation.MainScreen
import com.kostov.freetodolist.presentation.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ToDoNavGraph(
    navController: NavHostController
) {
    val viewModel: TaskViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("add_task") {
            AddTaskScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}