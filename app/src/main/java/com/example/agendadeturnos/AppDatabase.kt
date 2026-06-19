package com.example.agendadeturnos

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TurnoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun turnoDao(): TurnoDao
}
