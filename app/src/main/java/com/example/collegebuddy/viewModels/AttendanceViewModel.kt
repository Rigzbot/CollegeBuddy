package com.example.collegebuddy.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegebuddy.domain.Attendance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {
    private val database =
        FirebaseDatabase.getInstance("https://college-buddy-fc7e4-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("attendance")

    private val _classesAttended = MutableLiveData<String>()
    val classesAttended: LiveData<String> get() = _classesAttended

    private val _totalClasses = MutableLiveData<String>()
    val totalClasses: LiveData<String> get() = _totalClasses

    private val _isUpdated = MutableLiveData<Boolean>(false)
    val isUpdated: LiveData<Boolean> get() = _isUpdated

    var enrolmentNumber: String ?= null

    fun getAttendanceValues() {
        viewModelScope.launch {
            database.child(enrolmentNumber!!).child("attended").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _classesAttended.value = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            database.child(enrolmentNumber!!).child("total").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _totalClasses.value = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    fun changeIsUpdated() {
        _isUpdated.value = !isUpdated.value!!
    }

    fun updateAttendance(enrolment: String, attended: String, total: String) {
        val newAttended = _classesAttended.value?.toInt()?.plus(attended.toInt())
        val newTotal = _totalClasses.value?.toInt()?.plus(total.toInt())

        database.child(enrolment).setValue(Attendance(newTotal.toString(), newAttended.toString())).addOnCompleteListener {
            _isUpdated.value = true
        }

        _classesAttended.value = newAttended.toString()
        _totalClasses.value = newTotal.toString()
    }
}