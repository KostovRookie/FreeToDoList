package com.kostov.freetodolist.di

import android.app.Application
import androidx.room.Room
import com.kostov.freetodolist.data.local.TaskDatabase
import org.koin.dsl.module

val localModule = module {
    single {
        Room.databaseBuilder(
            get<Application>(),
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }

    single { get<TaskDatabase>().taskDao() }
}