package com.example.mapsapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.DrawerItem
import com.example.mapsapp.ui.navigation.InternalNavigationWrapper
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen() {
    // Creamos para controlar la navegación
    val navController = rememberNavController()

    // Creamos el estado del drawer (cerrado inicialmente)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Recordamos el ámbito de corrutinas para abrir/cerrar el drawer
    val scope = rememberCoroutineScope()

    // Guardamos el índice del ítem seleccionado
    var selectedItemIndex by remember { mutableStateOf(0) }

    // Definimos el contenedor principal con drawer lateral
    ModalNavigationDrawer(
        drawerContent = {
            // Mostramos la hoja del drawer con los ítems de navegación
            ModalDrawerSheet {
                DrawerItem.entries.forEachIndexed { index, drawerItem ->
                    NavigationDrawerItem(
                        icon = {
                            // Mostramos el icono del ítem
                            Icon(imageVector = drawerItem.icon, contentDescription = drawerItem.text)
                        },
                        label = {
                            // Mostramos el texto del ítem
                            Text(text = drawerItem.text)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            // Actualizamos el ítem seleccionado
                            selectedItemIndex = index

                            // Cerramos el drawer con una corrutina
                            scope.launch { drawerState.close() }

                            // Navegamos a la ruta correspondiente
                            navController.navigate(drawerItem.route)
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        // Definimos la estructura con TopAppBar y contenido principal
        Scaffold(
            topBar = {
                // Mostramos la barra superior con botón de menú
                TopAppBar(
                    title = { Text("Maps App") },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Abrimos el drawer al pulsar el icono de menú
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Cargamos el contenido de navegación, ajustando el padding
            InternalNavigationWrapper(navController, Modifier.padding(innerPadding))
        }
    }
}