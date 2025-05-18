package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.navigation.Destination.MarkerCreation
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DetailMarkerScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InternalNavigationWrapper(navController: NavHostController, modifier: Modifier) {

    // Definimos el host de navegación principal, empezando por la pantalla del mapa
    NavHost(navController, Destination.Map) {

        // Definimos la ruta para la pantalla del mapa
        composable<Destination.Map> {
            // Mostramos la pantalla de mapa y navegamos a la creación de marcador al pulsar
            MapsScreen(
                modifier = modifier,
                navigateToMarker = { latLng ->
                    navController.navigate(MarkerCreation(coordenadas = latLng))
                }
            )
        }

        // Definimos la ruta para la lista de marcadores
        composable<Destination.List> {
            // Mostramos la lista de marcadores y navegamos al detalle al seleccionar uno
            MarkerListScreen { id -> navController.navigate(Destination.MarkerDetail(id)) }
        }

        // Definimos la ruta para la creación de un nuevo marcador
        composable<MarkerCreation> { backStackEntry ->
            // Extraemos las coordenadas desde los argumentos
            val markerCreation = backStackEntry.toRoute<MarkerCreation>()
            // Mostramos la pantalla de creación de marcador y volvemos al mapa al finalizar
            CreateMarkerScreen(coordenadas = markerCreation.coordenadas) {
                navController.navigate(Destination.Map) {
                    popUpTo<Destination.Map> { inclusive = true }
                }
            }
        }

        // Definimos la ruta para el detalle de un marcador
        composable<Destination.MarkerDetail> { backStackEntry ->
            // Extraemos el ID del marcador desde los argumentos
            val markerDetail = backStackEntry.toRoute<Destination.MarkerDetail>()
            // Mostramos la pantalla de detalle y volvemos al mapa tras actualizar
            DetailMarkerScreen(markerId = markerDetail.id) {
                navController.navigate(Destination.Map) {
                    popUpTo<Destination.Map> { inclusive = true }
                }
            }
        }
    }
}
