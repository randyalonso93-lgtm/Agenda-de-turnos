package com.example.agendadeturnos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TurnoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun turnoDao(): TurnoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agenda_turnos_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
