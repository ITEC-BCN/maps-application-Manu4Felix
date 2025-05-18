package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp: Application() {

    companion object {
        // Definimos una variable global que contendrá el cliente de Supabase
        lateinit var database: MySupabaseClient
    }

    override fun onCreate() {
        super.onCreate()
        // Inicializamos el cliente de Supabase al iniciar la aplicación
        database = MySupabaseClient()
    }
}