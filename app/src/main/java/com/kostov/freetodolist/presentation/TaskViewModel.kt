package com.kostov.freetodolist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostov.freetodolist.data.TaskRepository
import com.kostov.freetodolist.data.local.TaskEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
}