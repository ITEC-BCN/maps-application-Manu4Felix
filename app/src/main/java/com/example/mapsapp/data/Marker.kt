package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: Int = 0,
    val title: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)

