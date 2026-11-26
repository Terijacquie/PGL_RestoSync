package com.example.pgl_restosync.Model.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Nota: Aquí voy englobar tanto comida como bebida, ya la tabla de categorias esta para ordenarlo

@Dao
interface PlatoDao {
    //1. Obtengo los platos de una categoría específica
    // Esto es necesario para lo que quiero, ya que si pulso "pizzas", quiero que solo me aparezcan los platos de esa categoría
    @Query("SELECT * FROM platos WHERE categoriaId = :categoriaId")
    fun obtenerPlatosPorCategoria(categoriaId: Int): Flow<List<PlatoEntity>>

    //2. Inserto los platos de la carta (bebidas también)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPlatos(platos: PlatoEntity)

    @Query("DELETE FROM platos")
    suspend fun deleteAllPlatos()
}