package com.example.pgl_restosync.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgl_restosync.Model.RestauranteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleComandaViewModel(private val repository: RestauranteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    private var mesaCargada: Int? = null
    fun cargarDetalleComanda(mesaId: Int) {
        if (mesaCargada == mesaId) return
        mesaCargada = mesaId

        viewModelScope.launch {
            _uiState.value = DetalleUiState.Cargando

            val numeroMesa = repository.obtenerNumeroMesa(mesaId)
            if (numeroMesa == null) {
                _uiState.value = DetalleUiState.Error("Mesa no encontrada")
                return@launch
            }

            repository.obtenerResumenComanda(mesaId).collect { lineas ->
                _uiState.value = DetalleUiState.Exito(numeroMesa, lineas)
            }
        }
    }

    fun eliminarDetalleComanda(detalleId: Int) {
        viewModelScope.launch {
            repository.eliminarDetalleComanda(detalleId)
        }
    }
}

