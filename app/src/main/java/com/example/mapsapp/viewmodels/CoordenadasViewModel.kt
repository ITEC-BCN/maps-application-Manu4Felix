package com.example.mapsapp.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class CoordenadasViewModel : ViewModel() {
    var selectedMarker: LatLng? = null
}