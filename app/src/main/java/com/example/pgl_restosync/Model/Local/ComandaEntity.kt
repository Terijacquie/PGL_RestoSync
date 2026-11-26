package com.example.pgl_restosync.Model.Local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comandas",
    foreignKeys = [
        ForeignKey(
            entity = MesaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mesaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ComandaEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mesaId: Int,
    val isActiva: Boolean = true,
)