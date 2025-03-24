package com.kostov.freetodolist.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kostov.freetodolist.data.local.Urgency
import com.kostov.freetodolist.presentation.composables.TaskGroupCard
import com.kostov.freetodolist.presentation.screens.addtask.TaskViewModel
import com.kostov.freetodolist.presentation.screens.profilescreen.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = koinViewModel(),
    userProfileViewModel: UserProfileViewModel = koinViewModel()
) {
    val tasks by taskViewModel.allTasks.collectAsState()

    val name by userProfileViewModel.userName.collectAsState()
    val profileImage by userProfileViewModel.profileImageUrl.collectAsState()

    val todayTasks = tasks.filter { it.date == LocalDate.now() }
    val todayProgress = if (todayTasks.isNotEmpty()) {
        todayTasks.count { it.isCompleted }.toFloat() / todayTasks.size
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = profileImage,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Hello!", color = Color.Gray, fontSize = 14.sp)
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6A3DE8)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Your today’s task almost done!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("main") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("View Task", color = Color(0xFF6A3DE8))
                    }
                }
                CircularProgressIndicator(
                    progress = { todayProgress },
                    modifier = Modifier.size(60.dp),
                    color = Color.White,
                    strokeWidth = 6.dp,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val urgencyGroups by taskViewModel.urgencyGroups.collectAsState(initial = emptyMap())

        Text("Tasks by Urgency", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Urgency.entries.forEach { urgency ->
            val tasksOfUrgency = urgencyGroups[urgency] ?: emptyList()
            if (tasksOfUrgency.isNotEmpty()) {
                val progress =
                    tasksOfUrgency.count { it.isCompleted }.toFloat() / tasksOfUrgency.size

                TaskGroupCard(
                    title = urgency.name.uppercase(),
                    subtitle = "${tasksOfUrgency.size} Tasks",
                    progress = progress,
                    color = when (urgency) {
                        Urgency.MINOR -> Color(0xFFBBDEFB)
                        Urgency.MEDIUM -> Color(0xFFFFF176)
                        Urgency.MAJOR -> Color(0xFFFF8A65)
                    },
                    icon = when (urgency) {
                        Urgency.MINOR -> Icons.Default.Settings
                        Urgency.MEDIUM -> Icons.Default.Warning
                        Urgency.MAJOR -> Icons.Default.Star
                    },
                    onClick = {
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}