package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.PermissionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper() {
    // Creamos y recordamos un controlador de navegación para gestionar la navegación entre pantallas
    val navController = rememberNavController()

    // Definimos el host de navegación principal, comenzando en la pantalla de permisos
    NavHost(navController, Destination.Permissions) {

        // Definimos la ruta para la pantalla de permisos
        composable<Destination.Permissions> {
            // Mostramos la pantalla de permisos y navegamos al Drawer si se aceptan
            PermissionScreen {
                navController.navigate(Destination.Drawer)
            }
        }

        // Definimos la ruta para la pantalla principal con Drawer
        composable<Destination.Drawer> {
            // Mostramos la pantalla con el menú lateral
            DrawerScreen()
        }
    }
}