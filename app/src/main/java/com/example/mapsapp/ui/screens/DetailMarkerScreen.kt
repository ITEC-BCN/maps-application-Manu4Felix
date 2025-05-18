package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MarkerViewModel
import java.io.File
import com.example.supabasetest.R


@Composable
fun DetailMarkerScreen(markerId: Int, navigateToBack: () -> Unit) {
    // Obtenemos el ViewModel para manejar los datos del marcador
    val markerViewModel = viewModel<MarkerViewModel>()

    // Cargamos el marcador específico por su ID
    markerViewModel.getMarker(markerId)

    // Observamos los valores del título y la descripción
    val markerTitle: String by markerViewModel.markerTitle.observeAsState("")
    val markerDescription: String by markerViewModel.markerDescription.observeAsState("")

    // Estados para gestionar imagen y diálogo
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    // Launcher para tomar foto
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri.value != null) {
            val stream = context.contentResolver.openInputStream(imageUri.value!!)
            bitmap.value = BitmapFactory.decodeStream(stream)
        }
    }

    // Launcher para elegir imagen de galería
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            val stream = context.contentResolver.openInputStream(it)
            bitmap.value = BitmapFactory.decodeStream(stream)
        }
    }

    // Fondo principal azul celeste
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta de contenido principal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB9F6CA)),
            border = BorderStroke(2.dp, Color.Blue)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de texto para editar el título
                TextField(
                    value = markerTitle,
                    onValueChange = { markerViewModel.editMarkerTitle(it) },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto para editar la descripción
                TextField(
                    value = markerDescription,
                    onValueChange = { markerViewModel.editMarkerDescription(it) },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen para abrir la cámara o galería
                Image(
                    painter = painterResource(id = R.drawable.camara),
                    contentDescription = "Abrir Cámara o Galería",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { showDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para guardar los cambios
                Button(
                    onClick = {
                        markerViewModel.updateMarker(markerId, markerTitle, markerDescription, bitmap.value)
                        navigateToBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text("Actualizar", color = Color.White)
                }
            }
        }

        // Diálogo de selección entre tomar foto o elegir de galería
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selecciona una opción") },
                text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        val uri = createImageUri2(context)
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
    }
}

// Función para crear una URI temporal para la imagen
fun createImageUri2(context: Context): Uri? {
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
