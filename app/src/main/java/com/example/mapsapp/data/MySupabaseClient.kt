package com.example.mapsapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mapsapp.utils.AuthState
import com.example.supabasetest.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MySupabaseClient {

    // Definimos el cliente principal de Supabase
    lateinit var client: SupabaseClient

    // Definimos el cliente de almacenamiento de Supabase
    lateinit var storage: Storage

    // Definimos la URL y clave del proyecto desde BuildConfig
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY

    // Creamos el cliente Supabase y configuramos Postgrest, Storage y Auth
    constructor() {
        client = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Postgrest) // Activamos consultas SQL
            install(Storage)  // Activamos almacenamiento de archivos
            install(Auth) { autoLoadFromStorage = true } // Activamos autenticación
        }
        // Asignamos el cliente de almacenamiento
        storage = client.storage
    }

    // Subimos una imagen al storage y devolvemos su URL pública
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images")
            .upload(path = "image_${fechaHoraActual.format(formato)}.png", data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    // Construimos la URL pública para acceder a una imagen
    fun buildImageUrl(imageFileName: String) =
        "${this.supabaseUrl}/storage/v1/object/public/images/${imageFileName}"

    // Eliminamos una imagen del storage usando su nombre
    suspend fun deleteImage(imageName: String) {
        val imgName = imageName.removePrefix("https://luxphgkqoavsmerxhoka.supabase.co/storage/v1/object/public/images/")
        client.storage.from("images").delete(imgName)
    }

    // Obtenemos todos los registros de la tabla Marker
    suspend fun getAllMarkers(): List<Marker> {
        return client.from("Marker").select().decodeList<Marker>()
    }

    // Obtenemos un único marcador filtrando por su id
    suspend fun getMarker(id: Int): Marker {
        return client.from("Marker").select {
            filter { eq("id", id) }
        }.decodeSingle<Marker>()
    }

    // Insertamos un nuevo marcador en la base de datos
    suspend fun insertMarker(marker: Marker) {
        client.from("Marker").insert(marker)
    }

    // Actualizamos los campos de un marcador específico, incluida la imagen
    suspend fun updateMarker(id: Int, title: String, description: String, imageName: String, imageFile: ByteArray) {
        val updatedImageName = storage.from("images").update(path = imageName, data = imageFile)
        client.from("Marker").update({
            set("title", title)
            set("description", description)
            set("image", buildImageUrl(imageFileName = updatedImageName.path))
        }) {
            filter { eq("id", id) }
        }
    }

    // Eliminamos un marcador de la base de datos por su id
    suspend fun deleteMarker(id: Int) {
        client.from("Marker").delete {
            filter { eq("id", id) }
        }
    }

    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

    suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

    fun retrieveCurrentSession(): UserSession?{
        val session = client.auth.currentSessionOrNull()
        return session
    }

    fun refreshSession(): AuthState {
        try {
            client.auth.currentSessionOrNull()
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

}

