package com.example.agendadeturnos
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "turnos")
data class TurnoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cliente: String,
    val servicio: String,
    val fechaHora: String,
    val estado: String
)
