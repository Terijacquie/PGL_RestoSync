package com.example.pgl_restosync.Model.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Nota: Lo pongo en vertical como en las claves foráneas porque es más comodo para estudiar
@Database(
    entities = [
        MesaEntity::class,
        CategoriaEntity::class,
        PlatoEntity::class,
        ComandaEntity::class,
        ComandaDetalleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    // ahora supongo yo que tengo que poner los metodos por cada DAO que se tenga que hacer
    abstract fun mesaDao(): MesaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun platoDao(): PlatoDao
    abstract fun comandaDao(): ComandaDao
    // voy a ver si safo de esta:
    //abstract fun comandaDetalleDao(): ComandaDetalleDao,

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restosync_room.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
