package com.example.myapplication.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.showGlobalToast

class SharedViewModel : ViewModel() {
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun showToast(message: String) {
        _toastMessage.value = message
        showGlobalToast(message)
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}