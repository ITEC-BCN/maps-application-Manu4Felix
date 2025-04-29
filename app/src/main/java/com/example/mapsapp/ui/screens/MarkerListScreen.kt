package com.example.mapsapp.ui.screens
/*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import com.example.mapsapp.viewmodels.MarkerViewModel

@Composable
fun ListScreen(navigateToDetails: (Int) -> Unit) {
    // Usamos el ViewModel para acceder a los datos de los personajes
    val myViewModel: MarkerViewModel = viewModel()

    // Observamos el estado de carga
    val showLoading: Boolean by myViewModel.showLoading.observeAsState(true)

    if (showLoading) {
        // Si se están cargando los datos, mostramos un indicador de progreso
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        // Filtramos los personajes según el texto introducido por el usuario
        val filteredList = personajes.items.filter {
            it.name.contains(searchText, ignoreCase = true)
        }

        // Mostramos la lista filtrada junto con el campo de búsqueda
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 16.dp, bottom = 130.dp)
        ) {
            // Mostramos el campo de texto para buscar personajes
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar personaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Mostramos la lista de personajes con LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Por cada personaje filtrado, llamamos al composable que lo representa
                items(filteredList) {
                    PersonajeItem(personaje = it, navigateToDetails)
                }
            }
        }
    }
}

@Composable
fun PersonajeItem(personaje: Item, navigateToDetails: (Int) -> Unit) {
    // Creamos una tarjeta clickable que representa a cada personaje
    Card(
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary), // Azul adaptable dependiendo modo
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Fondo dinámico
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(12.dp),
        onClick = { navigateToDetails(personaje.id) }
    ) {
        // Organizamos la imagen y el nombre del personaje en una fila
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Mostramos la imagen del personaje
            Image(
                painter = rememberAsyncImagePainter(model = personaje.image),
                contentDescription = personaje.name,
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, Color(0xFFF57C00))
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Mostramos el nombre con estilo y centrado verticalmente
            Text(
                text = personaje.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontFamily.Monospace
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
            )
        }
    }
}
*/