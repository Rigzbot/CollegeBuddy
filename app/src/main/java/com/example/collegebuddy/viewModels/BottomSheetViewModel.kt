package com.example.collegebuddy.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BottomSheetViewModel: ViewModel() {
    private val _idAddress = MutableLiveData<Uri>()
    val idAddress: LiveData<Uri> get() = _idAddress

    fun updateIdAddress(newAddress: Uri) {
        _idAddress.value = newAddress
    }
}