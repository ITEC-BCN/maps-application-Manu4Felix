package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp.Companion.database
import com.example.mapsapp.data.Marker
import com.example.mapsapp.data.MySupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MarkerViewModel : ViewModel() {

    // Creamos una instancia del cliente Supabase
    private val supabaseClient = MySupabaseClient()

    // Creamos un LiveData para mantener la lista de marcadores
    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    // Variable privada para guardar el marcador seleccionado
    private var _selectedMarker: Marker? = null

    // Creamos LiveData para el título del marcador
    private val _markerTitle = MutableLiveData<String>()
    val markerTitle = _markerTitle

    // Creamos LiveData para la descripción del marcador
    private val _markerDescription = MutableLiveData<String>()
    val markerDescription = _markerDescription

    // Definimos una función para obtener todos los marcadores de la base de datos
    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val markers = supabaseClient.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = markers
            }
        }
    }

    // Definimos una función para insertar un nuevo marcador en Supabase
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewMarker(id: Int, title: String, description: String, coordenadas: String, image: Bitmap?) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        CoroutineScope(Dispatchers.IO).launch {
            val imageName = database.uploadImage(stream.toByteArray())
            val newMarker = Marker(
                id = id,
                title = title,
                description = description,
                coordenadas = coordenadas,
                image = imageName
            )
            database.insertMarker(newMarker)
        }
    }

    // Definimos una función para actualizar un marcador existente
    fun updateMarker(id: Int, title: String, description: String, image: Bitmap?) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val imageName =
            _selectedMarker?.image?.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(
                id,
                title,
                description,
                imageName.toString(),
                stream.toByteArray()
            )
        }
    }

    // Definimos una función para eliminar un marcador y su imagen asociada
    fun deleteMarker(id: Int, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            supabaseClient.deleteImage(image)
            supabaseClient.deleteMarker(id)
            getAllMarkers()
        }
    }

    // Definimos una función para obtener los datos de un marcador específico
    fun getMarker(id: Int) {
        if (_selectedMarker == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = supabaseClient.getMarker(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerTitle.value = marker.title
                    _markerDescription.value = marker.description
                }
            }
        }
    }

    // Definimos una función para editar el título del marcador desde la vista
    fun editMarkerTitle(title: String) {
        _markerTitle.value = title
    }

    // Definimos una función para editar la descripción del marcador desde la vista
    fun editMarkerDescription(description: String) {
        _markerDescription.value = description
    }
}
