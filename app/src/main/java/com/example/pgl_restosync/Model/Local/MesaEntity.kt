package com.example.pgl_restosync.Model.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

// nuevo cambio 26/11/26: añadido el campo "forma" con un valor por defecto, esto permitirá automatizar la creación en la pantalla Home.
// (llamamos a la clase enum)
@Entity(tableName = "mesas")
data class MesaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numero: String,
    val forma: FormaMesa = FormaMesa.CUADRADA,
    val comensales: Int = 0,
    val isOpen: Boolean = false
    // el boolean lo utilizo para que, si una mesa está abierta, no se pueda clickear
)
