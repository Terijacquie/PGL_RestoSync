package com.example.pgl_restosync.Model.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    //1. Primero obtengo todas las categorias de la carta
    // Lo voy a usar para pintar las pestañas de la pantalla del pedido
    @Query("SELECT * FROM categorias")
    fun obtenerCategorias(): Flow<List<CategoriaEntity>>

    //2. Inserto las categorias de la carta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCategorias(categorias: CategoriaEntity)

    @Query("DELETE FROM categorias")
    suspend fun deleteAllCategorias()
}
