package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: Int = 0,
    val title: String,
    val coordenadas: String,
    val description: String,
    val image: String,
    val userID: Int = 0
)

