package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.mapsapp.data.Marker
import com.example.mapsapp.viewmodels.MarkerViewModel
import java.io.File
import com.example.supabasetest.R



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarkerScreen(coordenadas: String, navigateToBack: () -> Unit) {
    val markerViewModel = viewModel<MarkerViewModel>()
    val markersList by markerViewModel.markersList.observeAsState(emptyList<Marker>())
    markerViewModel.getAllMarkers()

    val markerTitle: String by markerViewModel.markerTitle.observeAsState("")
    val markerDescription: String by markerViewModel.markerDescription.observeAsState("")

    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri.value != null) {
                val stream = context.contentResolver.openInputStream(imageUri.value!!)
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri.value = it
                val stream = context.contentResolver.openInputStream(it)
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = markerTitle,
                onValueChange = { markerViewModel.editMarkerTitle(it) },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = markerDescription,
                onValueChange = { markerViewModel.editMarkerDescription(it) },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Selecciona una opción") },
                        text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                val uri = createImageUri(context)
                                imageUri.value = uri
                                launcher.launch(uri!!)
                            }) {
                                Text("Tomar Foto")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDialog = false
                                pickImageLauncher.launch("image/*")
                            }) {
                                Text("Elegir de Galería")
                            }
                        }
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.camara),
                    contentDescription = "Abrir Cámara o Galería",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { showDialog = true }
                )
            }

            Button(onClick = {
                markerViewModel.insertNewMarker(0, markerTitle, markerDescription, coordenadas, bitmap.value)
                navigateToBack()
            }) {
                Text("Add")
            }
        }
    }
}

fun createImageUri(context: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}