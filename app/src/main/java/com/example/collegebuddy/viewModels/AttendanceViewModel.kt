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

    private val _classesAttended = MutableLiveData<String>(null)
    val classesAttended: LiveData<String> get() = _classesAttended

    private val _classesAbsent = MutableLiveData<String>(null)
    val classesAbsent: LiveData<String> get() = _classesAbsent

    private val _isUpdated = MutableLiveData(false)
    val isUpdated: LiveData<Boolean> get() = _isUpdated

    var enrolmentNumber: String ?= null

    fun getAttendanceValues() {
        viewModelScope.launch {
            database.child(enrolmentNumber!!).child("attended").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _classesAttended.value = snapshot.value?.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            database.child(enrolmentNumber!!).child("absent").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _classesAbsent.value = snapshot.value?.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
    fun changeIsUpdated() {
        _isUpdated.value = !isUpdated.value!!
    }

    fun updateAttendance(enrolment: String, attended: String, absent: String) {
        var newAttendance = attended.toInt()
        var newAbsent = absent.toInt()
        if(!_classesAttended.value.isNullOrEmpty()){
            newAttendance += _classesAttended.value!!.toInt()
        }
        if(!_classesAbsent.value.isNullOrEmpty()){
            newAbsent += _classesAbsent.value!!.toInt()
        }

        database.child(enrolment).setValue(Attendance(newAbsent.toString(), newAttendance.toString())).addOnCompleteListener {
            _isUpdated.value = true
        }

        _classesAttended.value = newAttendance.toString()
        _classesAbsent.value = newAbsent.toString()
    }
}