package com.example.pgl_restosync.ViewModel

import com.example.pgl_restosync.Model.Local.AuxiliarDetalleComanda

sealed interface DetalleUiState {

    //Mientras se leen los datos de room
    object Cargando : DetalleUiState

    //Datos listos:
    data class Exito(
        val numeroMesa: String,
        val lineas: List<AuxiliarDetalleComanda>
    ) : DetalleUiState {
        val total: Double get() = lineas.sumOf { it.precio * it.cantidad }
    }

    // por si algo ha ido mal, como que una mesa no exista o algo asi
    data class Error(val mensaje: String) : DetalleUiState
}