package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient(
            supabaseUrl = "https://jjdhfbudsejebglbnaad.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpqZGhmYnVkc2VqZWJnbGJuYWFkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU4Mjc4ODYsImV4cCI6MjA2MTQwMzg4Nn0.ZJ8-r_TrJPOngQdEYQKqVEUBfdAWtGqRPkxQru90qoc"
        )
    }
}
