package com.example.mapsapp.data

import com.example.mapsapp.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class MySupabaseClient {

    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY

    val client: SupabaseClient

    init {
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
        }
    }

    // SQL operations
    suspend fun getAllMarkers(): List<Marker> {
        return client.from("Marker").select().decodeList<Marker>()
    }

    suspend fun getMarker(id: Int): Marker {
        return client.from("Marker").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marker>()
    }

    suspend fun insertMarker(marker: Marker) {
        client.from("Marker").insert(marker)
    }

    suspend fun updateMarker(id: Int, marker: Marker) {
        client.from("Marker").update(marker) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteMarker(id: Int) {
        client.from("Marker").delete {
            filter { eq("id", id) }
        }
    }
}
