package com.kostov.freetodolist.app

import android.app.Application
import com.kostov.freetodolist.di.localModule
import com.kostov.freetodolist.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ToDoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ToDoApp)
            modules(localModule, viewModelModule)
        }
    }
}