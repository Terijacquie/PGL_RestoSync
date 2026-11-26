package com.example.pgl_restosync.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgl_restosync.Model.Local.CategoriaEntity
import com.example.pgl_restosync.Model.Local.MesaEntity
import com.example.pgl_restosync.Model.Local.PlatoEntity
import com.example.pgl_restosync.Model.RestauranteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModelRestaurante(
    private val repository: RestauranteRepository
) : ViewModel() {

    init {
        inicializarBaseDatos()
    }

    // Nuevo: Asi cuando abro y cierro para las pruebas, se borra todo y se vuelve de 0
    private fun inicializarBaseDatos() {
        viewModelScope.launch {
            try{
            repository.reiniciarBaseDatos()
            repository.insertarDatosEjemplo()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 1. Conversión de la lista de mesas a StateFlow reactivo
    val mesas: StateFlow<List<MesaEntity>> = repository.todasLasMesas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //2. Conversión automática de la lista de categorias
    val categorias: StateFlow<List<CategoriaEntity>> = repository.todasLasCategorias
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //3. Abrir la mesa. Actualiza el estado, guarda los comensales y crea la comanda
    fun abrirMesa(mesaId: Int, comensales: Int) {
        viewModelScope.launch {
            //repository.actualizarEstadoMesa(mesaId, true, comensales)
            repository.abrirMesa(mesaId, comensales)
        }
    }

    //4. agrega un plato al detalle de la comanda de la mesa
    fun agregarPlatoMesa(mesaId: Int, platoId: Int, cantidad: Int) {
        viewModelScope.launch {
            repository.agregarPlatoMesa(mesaId, platoId, cantidad)
        }
    }

    // 5. nuevo : 26/11/26
    fun cerrarMesa(mesaId: Int) {
        viewModelScope.launch {
            repository.cerrarMesa(mesaId)
        }
    }

    //6. Elimina un plato del detalle de la comanda (lista)
    fun eliminarDetalleComanda(detalleId: Int) {
        viewModelScope.launch {
            repository.eliminarDetalleComanda(detalleId)
        }
    }

    // 7. Ctargoria seleccionada en el menu izquierdo, empezando en entrantes (id 1)
    private val _categoriaSeleccionada = MutableStateFlow(1)
    val categoriaSeleccionada: StateFlow<Int> = _categoriaSeleccionada

    //8. Platos de cada categoria, deben de cambiar solos al cambiar de categoria
    @OptIn(ExperimentalCoroutinesApi::class)
    val platos: StateFlow<List<PlatoEntity>> = _categoriaSeleccionada
        .flatMapLatest { categoriaId -> repository.obtenerPlatosPorCategoria(categoriaId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun seleccionarCategoria(categoriaId: Int) {
        _categoriaSeleccionada.value = categoriaId
    }
}
