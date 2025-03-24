package com.kostov.freetodolist.data


import com.kostov.freetodolist.data.local.TaskDao
import com.kostov.freetodolist.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    fun getAllTasks(): Flow<List<TaskEntity>> = dao.getAllTasks()

    suspend fun insertTask(task: TaskEntity) = dao.insertTask(task)

    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)

    suspend fun getTaskById(id: Int): TaskEntity? = dao.getTaskById(id)
}