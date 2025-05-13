package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.data.Marker
import com.example.mapsapp.ui.navigation.Destination.MarkerCreation
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DetailMarkerScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.viewmodels.MarkerViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InternalNavigationWrapper(navController: NavHostController, modifier: Modifier) {

    NavHost(navController, Destination.Map) {
        composable<Destination.Map> {
            MapsScreen(
                modifier = modifier,
                navigateToMarker = { latLng ->
                    navController.navigate(Destination.MarkerCreation(coordenadas = latLng))
                }
            )
        }
        composable<Destination.List>{
            MarkerListScreen { id -> navController.navigate(Destination.MarkerDetail(id)) }
        }

        composable<Destination.MarkerCreation> { backStackEntry ->
            val markerCreation = backStackEntry.toRoute<Destination.MarkerCreation>()
            CreateMarkerScreen(coordenadas = markerCreation.coordenadas) {
                navController.navigate(Destination.Map) {
                    popUpTo<Destination.Map> { inclusive = true }
                }
            }
        }
        composable<Destination.MarkerDetail> { backStackEntry ->
            val markerDetail = backStackEntry.toRoute<Destination.MarkerDetail>()
            DetailMarkerScreen(markerId = markerDetail.id){
                navController.navigate(Destination.Map) {
                    popUpTo<Destination.Map> { inclusive = true }
                }
            }
        }
    }
}
