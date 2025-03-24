package com.kostov.freetodolist.di

import com.kostov.freetodolist.data.TaskRepository
import com.kostov.freetodolist.presentation.screens.kitten.KittenViewModel
import com.kostov.freetodolist.presentation.screens.addtask.TaskViewModel
import com.kostov.freetodolist.presentation.screens.profilescreen.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { TaskRepository(get()) }

    viewModel { TaskViewModel(get()) }

    viewModel { KittenViewModel(get()) }

    viewModel { UserProfileViewModel(get()) }

}