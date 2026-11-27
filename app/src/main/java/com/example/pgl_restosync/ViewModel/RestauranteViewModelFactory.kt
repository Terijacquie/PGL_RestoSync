package com.example.pgl_restosync.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pgl_restosync.Model.RestauranteRepository

class RestauranteViewModelFactory(
    private val repository: RestauranteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Validación de instanciación de ViewModelRestaurante
        if(modelClass.isAssignableFrom(ViewModelRestaurante::class.java)) {
            return ViewModelRestaurante(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}
