package com.example.myapplication

import android.app.Application
import android.content.Context
import android.widget.Toast

class AppContext : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppContext? = null
        val context: Context
            get() = instance?.applicationContext
                ?: throw IllegalStateException("Application is not initialized")
    }
}

fun showGlobalToast(message: String) {
    Toast.makeText(AppContext.context, message, Toast.LENGTH_SHORT).show()
}