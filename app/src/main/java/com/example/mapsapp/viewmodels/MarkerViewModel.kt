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

    private val supabaseClient = MySupabaseClient()

    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedMarker: Marker? = null

    private val _markerTitle = MutableLiveData<String>()
    val markerTitle = _markerTitle

    private val _markerDescription = MutableLiveData<String>()
    val markerDescription = _markerDescription

    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val markers = supabaseClient.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = markers
            }
        }
    }

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

    fun updateMarker(id: Int, title: String, description: String, coordenadas: String, image: Bitmap?) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val imageName =
            _selectedMarker?.image?.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(
                id,
                title,
                coordenadas,
                description,
                imageName.toString(),
                stream.toByteArray()
            )
        }
    }

    fun deleteMarker(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            supabaseClient.deleteMarker(id)
            getAllMarkers()
        }
    }

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

    fun editMarkerTitle(title: String) {
        _markerTitle.value = title
    }

    fun editMarkerDescription(description: String) {
        _markerDescription.value = description
    }
}