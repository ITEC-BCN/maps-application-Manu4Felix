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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marker
import com.example.mapsapp.viewmodels.MarkerViewModel

@Composable
fun MarkerListScreen(navigateToDetails: (Int) -> Unit) {
    val markerViewModel: MarkerViewModel = viewModel()
    val markersList by markerViewModel.markersList.observeAsState(emptyList<Marker>())

    LaunchedEffect(Unit) {
        markerViewModel.getAllMarkers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp) // padding general
    ) {
        if (markersList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(0.6f).padding(top = 90.dp)
            ) {
                items(items = markersList) { marker ->
                    val dismissState = rememberSwipeToDismissBoxState()
                    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
                        dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                        LaunchedEffect(Unit) {
                            markerViewModel.deleteMarker(marker.id)
                        }
                    }
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    ) {
                        MarkerListItem(marker, navigateToDetails)
                    }
                }
            }
        }
    }
}


@Composable
fun MarkerListItem(marker: Marker, navigateToDetails: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .padding(vertical = 2.dp), // espacio arriba y abajo de cada ítem
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { navigateToDetails(marker.id) }

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = marker.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = marker.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Coordenadas: ${marker.coordenadas}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
