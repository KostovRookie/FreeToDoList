package com.kostov.freetodolist.di

import android.app.Application
import androidx.room.Room
import com.kostov.freetodolist.data.local.TaskDatabase
import com.kostov.freetodolist.data.remote.CatApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val localModule = module {
    single {
        Room.databaseBuilder(
            get<Application>(),
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }

    single { get<TaskDatabase>().taskDao() }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatApiService::class.java)
    }
}