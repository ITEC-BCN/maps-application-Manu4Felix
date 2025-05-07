package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerListScreen

@Composable
fun InternalNavigationWrapper(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, Destination.Map) {
        composable<Destination.Map> {
            MapsScreen(
                modifier = modifier,
                navigateToMarker = { coordenades ->
                    navController.navigate(Destination.MarkerCreation(coordenades))
                }
            )
        }
        composable<Destination.List>{
            MarkerListScreen ()
        }
        composable<Destination.MarkerCreation> {

            CreateMarkerScreen(
                navigateToDetail = { markerId ->
                    navController.navigate(Destination.MarkerDetail(markerId.toInt()))
                }

            )
        }
    }
}
