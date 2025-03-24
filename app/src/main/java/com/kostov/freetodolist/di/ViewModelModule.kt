package com.kostov.freetodolist.di

import com.kostov.freetodolist.data.TaskRepository
import com.kostov.freetodolist.presentation.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { TaskRepository(get()) }

    viewModel { TaskViewModel(get()) }
}