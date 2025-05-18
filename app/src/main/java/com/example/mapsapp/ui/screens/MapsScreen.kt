package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MarkerViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(modifier: Modifier = Modifier, navigateToMarker: (String) -> Unit) {
    // Obtenemos el ViewModel que contiene los marcadores
    val markerViewModel: MarkerViewModel = viewModel()

    // Observamos la lista de marcadores en tiempo real
    val marcadores = markerViewModel.markersList.observeAsState()

    // Solicitamos los marcadores cuando la pantalla se lanza
    LaunchedEffect(Unit) {
        markerViewModel.getAllMarkers()
    }

    // Estructuramos la pantalla
    Column(modifier.fillMaxSize()) {

        // Definimos una posición base (por ejemplo, la ubicación del ITB)
        val itb = LatLng(41.4534225, 2.1837151)

        // Creamos el estado de la cámara del mapa, enfocando en la posición base
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }

        // Mostramos el mapa de Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,

            // Detectamos una pulsación larga en el mapa y enviamos las coordenadas al callback
            onMapLongClick = { latLng ->
                val coordenadas = "${latLng.latitude},${latLng.longitude}"
                navigateToMarker(coordenadas)
            }
        ) {
            // Recorremos los marcadores y los mostramos en el mapa
            marcadores.value?.forEach { marker ->

                // Extraemos las coordenadas desde el formato guardado
                val cleanedLatLng = marker.coordenadas
                    .replace("lat/lng: (", "")
                    .replace(")", "")
                val latLngParts = cleanedLatLng.split(",")

                // Si el formato es válido, mostramos el marcador en el mapa
                if (latLngParts.size == 2) {
                    val lat = latLngParts[0].trim().toDouble()
                    val lon = latLngParts[1].trim().toDouble()
                    val position = LatLng(lat, lon)

                    // Dibujamos el marcador con título y descripción
                    Marker(
                        state = MarkerState(position = position),
                        title = marker.title,
                        snippet = marker.description
                    )
                } else {
                    // Mostramos un error si el formato de coordenadas no es válido
                    Log.e("MapsScreen", "Formato de LatLng incorrecto: ${marker.coordenadas}")
                }
            }
        }
    }
}
