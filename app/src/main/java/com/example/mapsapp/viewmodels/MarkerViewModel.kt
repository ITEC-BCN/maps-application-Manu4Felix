package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.data.Marker
import com.example.mapsapp.data.MySupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun insertNewMarker(title: String, description: String, latitud: Double, longitud: Double, image: String) {
        val newMarker = Marker(
            title = title,
            description = description,
            latitud = latitud,
            longitud = longitud,
            image = image
        )
        CoroutineScope(Dispatchers.IO).launch {
            supabaseClient.insertMarker(newMarker)
            getAllMarkers()
        }
    }

    fun updateMarker(id: Int, title: String, description: String, latitud: Double, longitud: Double, image: String) {
        val updatedMarker = Marker(
            id = id,
            title = title,
            description = description,
            latitud = latitud,
            longitud = longitud,
            image = image
        )
        CoroutineScope(Dispatchers.IO).launch {
            supabaseClient.updateMarker(id, updatedMarker)
            getAllMarkers()
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