package com.example.pgl_restosync.Model

import com.example.pgl_restosync.Model.Local.AuxiliarDetalleComanda
import com.example.pgl_restosync.Model.Local.CategoriaDao
import com.example.pgl_restosync.Model.Local.CategoriaEntity
import com.example.pgl_restosync.Model.Local.ComandaDao
import com.example.pgl_restosync.Model.Local.ComandaDetalleEntity
import com.example.pgl_restosync.Model.Local.ComandaEntity
import com.example.pgl_restosync.Model.Local.FormaMesa
import com.example.pgl_restosync.Model.Local.MesaDao
import com.example.pgl_restosync.Model.Local.MesaEntity
import com.example.pgl_restosync.Model.Local.PlatoDao
import com.example.pgl_restosync.Model.Local.PlatoEntity
import com.example.pgl_restosync.Model.Local.ResumenComanda
import kotlinx.coroutines.flow.Flow

class RestauranteRepository(
    private val mesaDao: MesaDao,
    private val categoriaDao: CategoriaDao,
    private val platoDao: PlatoDao,
    private val comandaDao: ComandaDao
) {
    // 1. Gestón de las mesas
    val todasLasMesas: Flow<List<MesaEntity>> = mesaDao.obtenerTodasLasMesas()
    // método de actualización de las mesas
    suspend fun actualizarEstadoMesa(mesaId: Int, isOpen: Boolean, comensales: Int) {
        mesaDao.actualizarEstadoMesa(mesaId, isOpen, comensales)
    }

    // 2. gestión de categorías y platos
    val todasLasCategorias: Flow<List<CategoriaEntity>> = categoriaDao.obtenerCategorias()
    // Este método obtiene los platos de la categoria entregada por el parametro categoriaId.
    // En teoría, solo debería reproducir los datos de los platos de esa categoría en concreto.
    fun obtenerPlatosPorCategoria(categoriaId: Int): Flow<List<PlatoEntity>> {
        return platoDao.obtenerPlatosPorCategoria(categoriaId)
    }

    // Abre la mesa y le crea comanda solo si no tenía ya una activa
    suspend fun abrirMesa(mesaId: Int, comensales: Int) {
        mesaDao.actualizarEstadoMesa(mesaId, true, comensales)
        if (comandaDao.obtenerComandaActivaId(mesaId) == null) {
            comandaDao.crearComanda(ComandaEntity(mesaId = mesaId))
        }
    }

    // Cierra la comanda y deja la mesa libre otra vez
    suspend fun cerrarMesa(mesaId: Int) {
        comandaDao.cerrarComandaActiva(mesaId)
        mesaDao.actualizarEstadoMesa(mesaId, false, 0)
    }

    // Añade un plato a la comanda actuva de la mesa
    suspend fun agregarPlatoMesa(mesaId: Int, platoId: Int, cantidad: Int) {
        val comandaId = comandaDao.obtenerComandaActivaId(mesaId) ?: return
        comandaDao.agregarPlatoDetalle(
            ComandaDetalleEntity(comandaId = comandaId, platoId = platoId, cantidad = cantidad)
        )
    }

    // Esto usa la clase auxiliar que cree con la interface ComandaDao, para que,
    // al hacer clic en una comanda activa, podamos ver un listado detallado de la comanda
    fun obtenerResumenComanda(mesaId: Int): Flow<List<AuxiliarDetalleComanda>> {
        return comandaDao.obtenerDetalleComanda(mesaId)
    }

    suspend fun eliminarDetalleComanda(detalleId: Int) {
        comandaDao.eliminarDetalleComanda(detalleId)
    }

    //Borrar todo para reiniciar cada arranque
    suspend fun reiniciarBaseDatos() {
        mesaDao.deleteAllMesas()
        categoriaDao.deleteAllCategorias()
        platoDao.deleteAllPlatos()
        //comandaDao.deleteAllComandaDetalle()
    }

    //27/11/2026
    // Lista de comandas para la pantalla del resumen
    val resumenComanda: Flow<List<ResumenComanda>> = comandaDao.obtenerResumenComandas()

    // Si la comanda estaba activa, la mesa se queda libre de nuevo
    suspend fun eliminarComanda(comanda: ResumenComanda) {
        comandaDao.borrarComanda(comanda.comandaId)
        if(comanda.isActiva){
            mesaDao.actualizarEstadoMesa(comanda.mesaId, false, 0)
        }
    }

    // En la siguiente sección, voy a hacer las inserciones necesarias
    suspend fun insertarDatosEjemplo() {
        // categorias
        categoriaDao.insertarCategorias(CategoriaEntity(id = 1, nombre = "Entrantes"))
        categoriaDao.insertarCategorias(CategoriaEntity(id = 2, nombre = "Ensaladas"))
        categoriaDao.insertarCategorias(CategoriaEntity(id = 3, nombre = "Pizzas"))
        categoriaDao.insertarCategorias(CategoriaEntity(id = 4, nombre = "Pastas"))
        categoriaDao.insertarCategorias(CategoriaEntity(id = 5, nombre = "Postres"))
        categoriaDao.insertarCategorias(CategoriaEntity(id = 6, nombre = "Bebidas"))

        // mesas
        // Nuevo cambio: añadido el campo "forma"
        mesaDao.insertarMesas(MesaEntity(id = 1, numero = "Mesa 1", comensales = 0, isOpen = false, forma = FormaMesa.CUADRADA))
        mesaDao.insertarMesas(MesaEntity(id = 2, numero = "Mesa 2", comensales = 0, isOpen = false, forma = FormaMesa.REDONDA_CUATRO_ASIENTOS))
        mesaDao.insertarMesas(MesaEntity(id = 3, numero = "Mesa 3", comensales = 0, isOpen = false, forma = FormaMesa.REDONDA_CINCO_ASIENTOS))
        mesaDao.insertarMesas(MesaEntity(id = 4, numero = "Mesa 4", comensales = 0, isOpen = false, forma = FormaMesa.RECTANGULAR))
        mesaDao.insertarMesas(MesaEntity(id = 5, numero = "Mesa 5", comensales = 0, isOpen = false, forma = FormaMesa.CUADRADA))
        mesaDao.insertarMesas(MesaEntity(id = 6, numero = "Mesa 6", comensales = 0, isOpen = false, forma = FormaMesa.REDONDA_CUATRO_ASIENTOS))

        // platos entrantes
        platoDao.insertarPlatos(PlatoEntity(id = 1, nombre = "Hummus de Berenjena", precio = 8.99, categoriaId = 1))
        platoDao.insertarPlatos(PlatoEntity(id = 2, nombre = "Focaccia Pomodori e Burrata", precio = 10.99, categoriaId = 1))
        // platos ensaladas
        platoDao.insertarPlatos(PlatoEntity(id = 3, nombre = "Cesar Romana", precio = 12.99, categoriaId = 2))
        platoDao.insertarPlatos(PlatoEntity(id = 4, nombre = "Ventresca e Mandorle", precio = 10.99, categoriaId = 2))
        // platos pizzas
        platoDao.insertarPlatos(PlatoEntity(id = 5, nombre = "Capriccio de Higo y Cabra", precio = 12.99, categoriaId = 3))
        platoDao.insertarPlatos(PlatoEntity(id = 6, nombre = "Pergamino di Porchetta", precio = 14.99, categoriaId = 3))
        // platos pastas
        platoDao.insertarPlatos(PlatoEntity(id = 7, nombre = "Gnocchi molto molto gorgonzola con jamón ibérico", precio = 11.99, categoriaId = 4))
        platoDao.insertarPlatos(PlatoEntity(id = 8, nombre = "Spaghetti Carbonara trufada", precio = 12.99, categoriaId = 4))
        // platos postres
        platoDao.insertarPlatos(PlatoEntity(id = 9, nombre = "Tiramisú", precio = 7.99, categoriaId = 5))
        platoDao.insertarPlatos(PlatoEntity(id = 10, nombre = "Panna-COCO-tta", precio = 5.99, categoriaId = 5))
        // bebidas
        platoDao.insertarPlatos(PlatoEntity(id = 11, nombre = "Estrella Galicia 1/3", precio = 2.99, categoriaId = 6))
        platoDao.insertarPlatos(PlatoEntity(id = 12, nombre = "Estrella Galicia 0,0", precio = 4.99, categoriaId = 6))






    }

}