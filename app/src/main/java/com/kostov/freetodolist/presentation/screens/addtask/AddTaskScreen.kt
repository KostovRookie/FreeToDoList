package com.kostov.freetodolist.presentation.screens.addtask

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kostov.freetodolist.data.local.TaskEntity
import com.kostov.freetodolist.data.local.TaskStatus
import com.kostov.freetodolist.data.local.Urgency
import com.kostov.freetodolist.presentation.screens.addtask.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel,
    navController: NavController,
    existingTask: TaskEntity? = null
) {
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var description by remember { mutableStateOf(existingTask?.description ?: "") }
    var selectedUrgency by remember { mutableStateOf(existingTask?.urgency ?: Urgency.MEDIUM) }
    var startDate by remember { mutableStateOf(existingTask?.date ?: LocalDate.now()) }
    var endDate by remember {
        mutableStateOf(
            existingTask?.date?.plusDays(1) ?: LocalDate.now().plusDays(1)
        )
    }

    val context = LocalContext.current
    DateTimeFormatter.ofPattern("dd MMM, yyyy")

    fun showDatePicker(currentDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, day -> onDateSelected(LocalDate.of(year, month + 1, day)) },
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (existingTask != null) "Edit Task" else "Add Task")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Task Urgency", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F4FA), RoundedCornerShape(16.dp))
                    .clickable { expanded = true }
                    .padding(16.dp)
            ) {
                Text(selectedUrgency.name)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Urgency.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                selectedUrgency = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Task Name", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F4FA), RoundedCornerShape(16.dp)),
                singleLine = true,
                placeholder = { Text("e.g. Grocery Shopping App") }
            )

            Spacer(Modifier.height(16.dp))

            Text("Description", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F4FA), RoundedCornerShape(16.dp)),
                placeholder = { Text("Project description...") },
                maxLines = 5
            )

            Spacer(Modifier.height(16.dp))

            Text("Start Date", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            DateInputBlock(date = startDate, onClick = {
                showDatePicker(startDate) { startDate = it }
            })

            Spacer(Modifier.height(16.dp))

            Text("End Date", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            DateInputBlock(date = endDate, onClick = {
                showDatePicker(endDate) { endDate = it }
            })

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val task = TaskEntity(
                        id = existingTask?.id ?: 0,
                        title = title,
                        description = description,
                        date = startDate,
                        time = existingTask?.time ?: LocalTime.now(),
                        urgency = selectedUrgency,
                        status = existingTask?.status ?: TaskStatus.TODO,
                        isCompleted = existingTask?.isCompleted ?: false
                    )
                    if (existingTask != null) {
                        viewModel.updateTask(task)
                    } else {
                        viewModel.insertTask(task)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A3DE8))
            ) {
                Text(
                    text = if (existingTask != null) "Update" else "Add a task",
                    color = Color.White
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateInputBlock(date: LocalDate, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F4FA), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF6A3DE8))
        Spacer(Modifier.width(12.dp))
        Text(text = date.format(DateTimeFormatter.ofPattern("dd MMM, yyyy")))
    }
}