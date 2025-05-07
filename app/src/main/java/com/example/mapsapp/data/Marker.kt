package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: Int = 0,
    val title: String,
    val image: String,
    val latitud: Double,
    val longitud: Double,
    val description: String
)

