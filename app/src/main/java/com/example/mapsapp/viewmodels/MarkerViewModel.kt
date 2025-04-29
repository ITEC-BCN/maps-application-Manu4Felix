package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarkerViewModel: ViewModel() {

    val database = MyApp.database
    private val _studentName = MutableLiveData<String>()
    val studentName = _studentName
    private val _studentMark = MutableLiveData<String>()
    val studentMark = _studentMark
    fun insertNewStudent(name: String, mark: String) {
        val newStudent = Student(name = name, mark = mark.toDouble())
        CoroutineScope(Dispatchers.IO).launch {
            database.insertStudent(newStudent)
            database.getAllStudents()
        }
    }

}