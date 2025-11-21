package com.example.myapplication

import android.app.Application
import android.content.Context
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: AppContext? = null
        val context: Context
            get() = instance?.applicationContext
                ?: throw IllegalStateException("Application is not initialized")
    }
}

// ✅ Función global para mostrar un Toast desde cualquier parte del código
fun showGlobalToast(message: String) {
    Toast.makeText(AppContext.context, message, Toast.LENGTH_SHORT).show()
}