package com.example.collegebuddy.viewModels

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val _ttAddress = MutableLiveData<Uri>()
    val ttAddress: LiveData<Uri> get() = _ttAddress

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDayOfWeek = LocalDate.now().dayOfWeek.name

    fun updateTTAddress(newAddress: Uri) {
        _ttAddress.value = newAddress
    }
}