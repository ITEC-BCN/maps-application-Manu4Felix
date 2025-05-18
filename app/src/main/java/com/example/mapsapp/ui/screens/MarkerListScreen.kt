package com.example.mapsapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marker
import com.example.mapsapp.viewmodels.MarkerViewModel

@Composable
fun MarkerListScreen(navigateToDetails: (Int) -> Unit) {
    // Obtenemos una instancia del ViewModel
    val markerViewModel: MarkerViewModel = viewModel()

    // Observamos los marcadores en tiempo real
    val markersList by markerViewModel.markersList.observeAsState(emptyList<Marker>())

    // Llamamos a la función para cargar los marcadores al iniciar la pantalla
    LaunchedEffect(Unit) {
        markerViewModel.getAllMarkers()
    }

    // Establecemos el fondo principal y estructura general de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            // Mostramos el título centrado en la parte superior
            Text(
                text = "Mis Marcadores",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFb0bec5),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp, bottom = 16.dp)
            )

            // Mostramos un indicador de carga si la lista aún está vacía
            if (markersList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFb0bec5))
                }
            } else {
                // Mostramos la lista de marcadores en una LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f)
                        .padding(top = 40.dp)
                ) {
                    // Iteramos cada marcador para mostrarlo
                    items(items = markersList) { marker ->
                        // Creamos el estado de swipe para detectar deslizamientos
                        val dismissState = rememberSwipeToDismissBoxState()

                        // Si se confirma el deslizamiento, eliminamos el marcador
                        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
                            dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                            LaunchedEffect(Unit) {
                                markerViewModel.deleteMarker(marker.id)
                            }
                        }

                        // Definimos el comportamiento y fondo del swipe
                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFFB3E5FC)),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.Red,
                                        modifier = Modifier.padding(24.dp)
                                    )
                                }
                            }
                        ) {
                            // Mostramos el marcador con su diseño personalizado
                            MarkerListItem(marker, navigateToDetails)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarkerListItem(marker: Marker, navigateToDetails: (Int) -> Unit) {
    // Creamos una tarjeta interactiva con sombra y borde
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(3.dp, Color.Blue),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB9F6CA)),
        onClick = { navigateToDetails(marker.id) } // Navegamos al detalle del marcador
    ) {
        // Mostramos la información del marcador en columnas
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Mostramos el título en negrita
            Text(
                text = marker.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Mostramos la descripción
            Text(
                text = marker.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Mostramos las coordenadas
            Text(
                text = "Coordenadas: ${marker.coordenadas}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
