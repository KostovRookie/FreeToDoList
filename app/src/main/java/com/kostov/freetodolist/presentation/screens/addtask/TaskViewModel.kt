package com.kostov.freetodolist.presentation.screens.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostov.freetodolist.data.TaskRepository
import com.kostov.freetodolist.data.local.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    val urgencyGroups = allTasks.map { tasks ->
        tasks.groupBy { it.urgency }
    }
    private val _selectedTask = MutableStateFlow<TaskEntity?>(null)
    val selectedTask: StateFlow<TaskEntity?> = _selectedTask

    fun insertTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun loadTaskById(id: Int) {
        viewModelScope.launch {
            _selectedTask.value = repository.getTaskById(id)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

}