package com.kostov.freetodolist.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kostov.freetodolist.presentation.screens.addtask.AddTaskScreen
import com.kostov.freetodolist.presentation.screens.home.HomeScreen
import com.kostov.freetodolist.presentation.screens.kitten.KittenScreen
import com.kostov.freetodolist.presentation.screens.main.MainScreen
import com.kostov.freetodolist.presentation.screens.profilescreen.ProfileScreen
import com.kostov.freetodolist.presentation.screens.addtask.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ToDoNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: TaskViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                navController = navController
            )
        }

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

        composable("edit_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            if (taskId != null) {
                viewModel.loadTaskById(taskId)
                val selectedTask = viewModel.selectedTask.collectAsState()

                selectedTask.value?.let { task ->
                    AddTaskScreen(
                        viewModel = viewModel,
                        navController = navController,
                        existingTask = task
                    )
                }
            }
        }

        composable("kitten") {
            KittenScreen()
        }

        composable("profile") {
            ProfileScreen()
        }
    }
}
