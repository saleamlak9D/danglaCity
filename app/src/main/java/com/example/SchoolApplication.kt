package com.example

import android.app.Application
import com.example.data.SchoolDatabase
import com.example.data.SchoolRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SchoolApplication : Application() {
    val database by lazy { SchoolDatabase.getDatabase(this) }
    val repository by lazy { SchoolRepository(database.schoolDao()) }

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        // Seed database asynchronously on app start
        applicationScope.launch {
            repository.seedDataIfEmpty()
        }
    }
}
