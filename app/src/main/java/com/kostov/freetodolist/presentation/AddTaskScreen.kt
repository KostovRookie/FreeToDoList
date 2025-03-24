package com.kostov.freetodolist.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kostov.freetodolist.data.local.TaskEntity
import com.kostov.freetodolist.data.local.Urgency
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }
    var selectedUrgency by remember { mutableStateOf(Urgency.MEDIUM) }

    // Date Picker
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            selectedDate = LocalDate.of(year, month + 1, day)
        },
        selectedDate.year,
        selectedDate.monthValue - 1,
        selectedDate.dayOfMonth
    )

    // Time Picker
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            selectedTime = LocalTime.of(hour, minute)
        },
        selectedTime.hour,
        selectedTime.minute,
        true
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Task") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Date: ${selectedDate}")
            }

            Button(
                onClick = { timePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Time: ${selectedTime}")
            }

            // Urgency Dropdown
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedUrgency.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Urgency") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Urgency.values().forEach { urgency ->
                        DropdownMenuItem(
                            text = { Text(urgency.name) },
                            onClick = {
                                selectedUrgency = urgency
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.text.isNotBlank()) {
                        val newTask = TaskEntity(
                            title = title.text,
                            description = description.text,
                            date = selectedDate,
                            time = selectedTime,
                            urgency = selectedUrgency
                        )
                        viewModel.insertTask(newTask)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}