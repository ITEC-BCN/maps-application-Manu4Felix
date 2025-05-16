package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.CoordenadasViewModel
import com.example.mapsapp.viewmodels.MarkerViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(modifier: Modifier = Modifier, navigateToMarker: (String) -> Unit) {
    val coordenadasViewModel: CoordenadasViewModel = viewModel()
    val markerViewModel: MarkerViewModel = viewModel()
    val marcadores = markerViewModel.markersList.observeAsState()

    LaunchedEffect(Unit) {
        markerViewModel.getAllMarkers()
    }

    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                val coordenadas = "${latLng.latitude},${latLng.longitude}"
                navigateToMarker(coordenadas)
            }
        ) {
            marcadores.value?.forEach { marker ->

                val cleanedLatLng = marker.coordenadas
                    .replace("lat/lng: (", "")
                    .replace(")", "")
                val latLngParts = cleanedLatLng.split(",")
                if (latLngParts.size == 2) {
                    val lat = latLngParts[0].trim().toDouble()
                    val lon = latLngParts[1].trim().toDouble()
                    val position = LatLng(lat, lon)
                    Marker(
                        state = MarkerState(position = position),
                        title = marker.title,
                        snippet = marker.description
                    )
                } else {
                    Log.e("MapsScreen", "Formato de LatLng incorrecto: ${marker.coordenadas}")
                }
            }
        }
    }
}