package com.example.mapsapp.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.PermissionStatus
import com.example.mapsapp.viewmodels.PermissionViewModel

@Composable
fun PermissionScreen(navigateToDrawer: () -> Unit) {
    // Obtenemos la actividad actual del contexto
    val activity = LocalContext.current as? Activity

    // Obtenemos el ViewModel encargado del manejo de permisos
    val viewModel = viewModel<PermissionViewModel>()

    // Definimos la lista de permisos a solicitar
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    // Obtenemos el estado actual de cada permiso desde el ViewModel
    val permissionsStatus = viewModel.permissionsStatus.value

    // Controlamos si ya se solicitó el permiso previamente
    var alreadyRequested by remember { mutableStateOf(false) }

    // Definimos el lanzador de actividad para solicitar múltiples permisos
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result: Map<String, Boolean> ->
        // Procesamos el resultado de cada permiso solicitado
        permissions.forEach { permission ->
            val granted = result[permission] ?: false
            val status = when {
                granted -> PermissionStatus.Granted
                ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission) ->
                    PermissionStatus.Denied
                else -> PermissionStatus.PermanentlyDenied
            }
            // Actualizamos el estado del permiso en el ViewModel
            viewModel.updatePermissionStatus(permission, status)
        }
    }

    // Lanzamos la solicitud de permisos una sola vez al iniciar
    LaunchedEffect(Unit) {
        if (!alreadyRequested) {
            alreadyRequested = true
            launcher.launch(permissions.toTypedArray())
        }
    }

    // Mostramos la UI principal centrada en pantalla
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostramos el título de la sección
        Text("Permissions status:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        // Recorremos cada permiso para mostrar su estado
        permissions.forEach { permission ->
            val status = permissionsStatus[permission]
            val label = when (status) {
                null -> "Requesting..."
                PermissionStatus.Granted -> "Granted"
                PermissionStatus.Denied -> "Denied"
                PermissionStatus.PermanentlyDenied -> "Permanently denied"
            }
            val permissionName = permission.removePrefix("android.permission.")
            Text("$permissionName: $label")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostramos un botón para volver a pedir permisos si alguno fue denegado
        if (permissions.any {
                permissionsStatus[it] == PermissionStatus.Denied
            }
        ) {
            Button(onClick = {
                launcher.launch(permissions.toTypedArray())
            }) {
                Text("Apply again")
            }
        } else {
            // Si todos los permisos son aceptados, navegamos a la siguiente pantalla
            navigateToDrawer()
        }

        // Mostramos un botón para abrir ajustes si algún permiso fue denegado permanentemente
        if (permissions.any {
                permissionsStatus[it] == PermissionStatus.PermanentlyDenied
            }
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity!!.packageName, null)
                }
                activity!!.startActivity(intent)
            }) {
                Text("Go to settings")
            }
        } else {
            // Si no hay permisos bloqueados, navegamos a la siguiente pantalla
            navigateToDrawer()
        }
    }
}
