package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MarkerViewModel: ViewModel() {

    // Creamos un LiveData para controlar si mostrar el indicador de carga
    private val _showLoading = MutableLiveData<Boolean>(true)
    val showLoading = _showLoading
}