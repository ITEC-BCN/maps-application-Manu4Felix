package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MarkerViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

//@Composable
//fun StudentsScreen(navigateToDetail: (String) -> Unit) {
//    val myViewModel = viewModel<MarkerViewModel>()
//    val studentName: String by myViewModel.studentName.observeAsState("")
//    val studentMark: String by myViewModel.studentMark.observeAsState("")
//    Column(Modifier.fillMaxSize()) {
//        Column(Modifier.fillMaxWidth().weight(0.4f),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center) {
//            Text("Create new student", fontSize = 28.sp, fontWeight = FontWeight.Bold)
//            TextField(value = studentName, onValueChange = { myViewModel.editStudentName(it) })
//            TextField(value = studentMark, onValueChange = { myViewModel.editStudentMark(it) })
//            Button(onClick = { myViewModel.insertNewStudent(studentName, studentMark) }) {
//                Text("Insert")
//            }
//        }
//    }
//}
