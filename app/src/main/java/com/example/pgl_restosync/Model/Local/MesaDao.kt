package com.example.pgl_restosync.Model.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MesaDao {
    // 1. Obtenemos todas las mesas para el home
    @Query("SELECT * FROM mesas")
    fun obtenerTodasLasMesas(): Flow<List<MesaEntity>>

    //2. Actualizamos el estado isOpen y el número de comensales
    @Query("UPDATE mesas SET isOpen = :isOpen, comensales = :comensales WHERE id = :mesaId")
    suspend fun actualizarEstadoMesa(mesaId: Int, isOpen: Boolean, comensales: Int)

    //3. Insertamos las mesas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMesas(mesas: MesaEntity)

    @Query("DELETE FROM mesas")
    suspend fun deleteAllMesas()
}
