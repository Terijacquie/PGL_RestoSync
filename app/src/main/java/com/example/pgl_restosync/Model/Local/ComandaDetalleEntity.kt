package com.example.pgl_restosync.Model.Local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comanda_detalle",
    foreignKeys = [
        ForeignKey(
            entity = ComandaEntity::class,
            parentColumns = ["id"],
            childColumns = ["comandaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ComandaDetalleEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val comandaId: Int,
    val platoId: Int,
    val cantidad: Int
)