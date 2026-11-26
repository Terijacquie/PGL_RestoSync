package com.example.pgl_restosync.Model.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mesas")
data class MesaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numero: String,
    val comensales: Int = 0,
    val isOpen: Boolean = false
    // el boolean lo utilizo para que, si una mesa está abierta, no se pueda clickear
)
