package com.example.mapsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mapsapp.ui.navigation.MainNavigationWrapper
import com.example.mapsapp.ui.theme.MapsAppTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Llamamos a la función para mostrar el Splash Screen al iniciar la app
        installSplashScreen()

        // Activamos el modo de pantalla completa
        enableEdgeToEdge()

        // Definimos el contenido de la pantalla utilizando Jetpack Compose
        setContent {
            // Aplicamos el tema de la aplicación
            MapsAppTheme {
                // Llamamos al contenedor principal de la navegación
                MainNavigationWrapper()
            }
        }
    }
}