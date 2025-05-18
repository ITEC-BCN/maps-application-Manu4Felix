package com.example.mapsapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapsapp.utils.PermissionStatus
import androidx.compose.runtime.State

class PermissionViewModel: ViewModel() {

    // Creamos un estado mutable para almacenar el estado de los permisos en un mapa
    private val _permissionsStatus = mutableStateOf<Map<String, PermissionStatus>>(emptyMap())

    // Definimos un State público e inmutable para que la UI observe el estado de los permisos
    val permissionsStatus: State<Map<String, PermissionStatus>> = _permissionsStatus

    // Definimos una función para actualizar el estado de un permiso específico
    fun updatePermissionStatus(permission: String, status: PermissionStatus) {
        _permissionsStatus.value = _permissionsStatus.value.toMutableMap().apply {
            this[permission] = status
        }
    }
}