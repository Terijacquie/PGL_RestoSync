package com.example.pgl_restosync

import android.app.Application
import com.example.pgl_restosync.Model.Local.AppDatabase
import com.example.pgl_restosync.Model.RestauranteRepository
import kotlin.getValue

//Recordatorio: se crea al arrancar la app antes de cualquier pantalla
//Guarda la base de datos y el repositorio para que todos los viewmodels usen los mismos
class RestoSyncApp: Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    val repository: RestauranteRepository by lazy {
        RestauranteRepository(
            database.mesaDao(),
            database.categoriaDao(),
            database.platoDao(),
            database.comandaDao()
        )
    }
}