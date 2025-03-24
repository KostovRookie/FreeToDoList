package com.kostov.freetodolist.presentation.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kostov.freetodolist.data.local.TaskStatus
import java.time.LocalDate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import com.kostov.freetodolist.data.local.TaskEntity
import com.kostov.freetodolist.presentation.screens.addtask.TaskViewModel
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    val tasks by viewModel.allTasks.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedStatus by remember { mutableStateOf<TaskStatus?>(null) }

    val filteredTasks = tasks.filter {
        it.date == selectedDate && (selectedStatus == null || it.status == selectedStatus)
    }

    Scaffold(
        containerColor = Color(0xFFEDEBFF)

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Today’s Tasks",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            DateStrip(selectedDate = selectedDate, onDateChange = { selectedDate = it })

            Spacer(modifier = Modifier.height(16.dp))

            StatusFilterRow(selectedStatus = selectedStatus) {
                selectedStatus = it
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredTasks) { task ->
                    ModernTaskCard(
                        task = task,
                        onEditClick = {
                            navController.navigate("edit_task/${task.id}")
                        },
                        onDeleteClick = {
                            viewModel.deleteTask(task)
                        },
                        onToggleComplete = {
                            val isNowCompleted = !task.isCompleted
                            viewModel.updateTask(
                                task.copy(
                                    isCompleted = isNowCompleted,
                                    status = if (isNowCompleted) TaskStatus.DONE else TaskStatus.TODO
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateStrip(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong())
    val days = (0..20).map { startOfWeek.plusDays(it.toLong()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(days) { date ->
                val isSelected = date == selectedDate
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFF6A3DE8) else Color.White)
                        .clickable { onDateChange(date) }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.month.name.take(3).uppercase() + " ${date.dayOfMonth}",
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 12.sp
                    )
                    Text(
                        text = date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
                        color = if (isSelected) Color.White else Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (selectedDate != today) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onDateChange(today) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A3DE8))
            ) {
                Text("Return to Current Day", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatusFilterRow(
    selectedStatus: TaskStatus?,
    onStatusChange: (TaskStatus?) -> Unit
) {
    val statuses = listOf(null) + TaskStatus.entries.toTypedArray()
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        statuses.forEach { status ->
            val isSelected = selectedStatus == status
            Button(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                onClick = { onStatusChange(status) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color(0xFF6A3DE8) else Color(0xFFF1EDFC),
                    contentColor = if (isSelected) Color.White else Color.Black
                )
            ) {
                Text(text = status?.name?.replace("_", " ") ?: "All", fontSize = 12.sp)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModernTaskCard(
    task: TaskEntity,
    onEditClick: (TaskEntity) -> Unit,
    onDeleteClick: (TaskEntity) -> Unit,
    onToggleComplete: (TaskEntity) -> Unit
) {
    val statusColor = when (task.status) {
        TaskStatus.TODO -> Color(0xFF2D9CDB)
        TaskStatus.IN_PROGRESS -> Color(0xFFF2994A)
        TaskStatus.DONE -> Color(0xFF27AE60)
    }

    val statusBg = when (task.status) {
        TaskStatus.TODO -> Color(0xFFE3F2FD)
        TaskStatus.IN_PROGRESS -> Color(0xFFFFF3E0)
        TaskStatus.DONE -> Color(0xFFE8F5E9)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.description, fontSize = 12.sp, color = Color.Gray)
            Text(task.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF6A3DE8),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    val formattedTime = task.time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    Text(text = formattedTime, fontSize = 12.sp, color = Color(0xFF6A3DE8))                }

                Text(
                    text = when (task.status) {
                        TaskStatus.TODO -> "To-do"
                        TaskStatus.IN_PROGRESS -> "In Progress"
                        TaskStatus.DONE -> "Done"
                    },
                    fontSize = 12.sp,
                    color = statusColor,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { onToggleComplete(task) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (task.isCompleted) Color(0xFF9B51E0) else Color(0xFF27AE60)
                    )
                ) {
                    Text(if (task.isCompleted) "Mark Incomplete" else "Mark Complete")
                }

                OutlinedButton(
                    onClick = { onEditClick(task) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = { onDeleteClick(task) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}