package com.example.pgl_restosync.Model.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Tengo que preguntar al profesor como se hace lo de las clases auxiliares
data class AuxiliarDetalleComanda(
    val id: Int,
    val platoNombre: String,
    val precio: Double,
    val cantidad: Int
)

@Dao
interface ComandaDao {
    // 1. Creamos la cabecera de la comanda
    // Room (SQLite) pide que se devuelva un Long por norma general porque SQLite
    // devuelve a Room este tipo de dato, asi que exigen que se retorne este tipo de dato
    @Insert
    suspend fun crearComanda(comanda: ComandaEntity): Long

    // 2. Añdir un plato al detalle de la comanda
    @Insert
    suspend fun agregarPlatoDetalle(platoDetalle: ComandaDetalleEntity)

    //3. Consultar los platos y cantidades de la comanda de una mesa
    // Esta es la consulta más larga
    @Query(
        """
        SELECT p.nombre AS platoNombre, p.precio, d.cantidad
        FROM comanda_detalle d
        INNER JOIN comandas c ON d.comandaId = c.id
        INNER JOIN platos p ON d.platoId = p.id
        WHERE c.mesaId = :mesaId AND c.isActiva = 1
        """
    )
    fun obtenerDetalleComanda(mesaId: Int): Flow<List<AuxiliarDetalleComanda>>

    // nuevo: Eliminar
    @Query("DELETE FROM comanda_detalle WHERE id = :detalleId")
    suspend fun eliminarDetalleComanda(detalleId: Int)

    @Query("DELETE FROM comanda_detalle")
    suspend fun deleteAllComandaDetalle()

    // Lógica de mesas y comandas:
    // 1. Devuelve el id de la comanda abierta de esa mesa, o null si no tiene ninguna
    @Query("SELECT id FROM comandas WHERE mesaId = :mesaId AND isActiva = 1 LIMIT 1")
    suspend fun obtenerComandaActivaId(mesaId: Int): Int?

    // 2. Cierra la comanda: no se borra, se queda como histórico
    @Query("UPDATE comandas SET isActiva = 0 WHERE mesaId = :mesaId AND isActiva = 1")
    suspend fun cerrarComandaActiva(mesaId: Int)

}
