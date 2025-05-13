package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.CoordenadasViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(modifier: Modifier = Modifier, navigateToMarker: (String) -> Unit) {
    val coordenadasViewModel: CoordenadasViewModel = viewModel()
    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }
        GoogleMap(
            Modifier.fillMaxSize(), cameraPositionState = cameraPositionState,
            onMapClick = {
                Log.d("MAP CLICKED", it.toString())
            }, onMapLongClick = { latLng ->
                navigateToMarker(latLng.toString())
            }) {
            Marker(
                state = MarkerState(position = itb)
            )
            coordenadasViewModel.selectedMarker?.let {
                Marker(
                    state = MarkerState(position = it)
                )
            }
        }
    }
}