package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MarkerViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun DetailMarkerScreen(markerId: String, navigateBack: () -> Unit) {
    val markerViewModel = viewModel<MarkerViewModel>()
    markerViewModel.getMarker(markerId.toInt())
    val markerTitle: String by markerViewModel.markerTitle.observeAsState("")
    val markerDescription: String by markerViewModel.markerDescription.observeAsState("")
    val markerLatitud: String by markerViewModel.markerTitle.observeAsState("")
    val markerLongitud: String by markerViewModel.markerDescription.observeAsState("")

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = markerTitle, onValueChange = { markerViewModel.editMarkerTitle(it) }, label = { Text("Title") })
        TextField(value = markerDescription, onValueChange = { markerViewModel.editMarkerDescription(it) }, label = { Text("Description") })
        TextField(value = markerLatitud, onValueChange = { markerViewModel.editMarkerTitle(it) }, label = { Text("Title") })
        TextField(value = markerDescription, onValueChange = { markerViewModel.editMarkerDescription(it) }, label = { Text("Description") })
//        Button(onClick = {
//            markerViewModel.insertNewMarker(
//                markerId.toInt(),
//                markerTitle,
//                markerDescription,
//                LatLng(markerLatitud.toDouble(), markerLongitud.toDouble()),
//                null // Cambiar "" por null para un Bitmap?
//            )
//            navigateBack()
//        }) {
//            Text("Update")
//        }
    }
}