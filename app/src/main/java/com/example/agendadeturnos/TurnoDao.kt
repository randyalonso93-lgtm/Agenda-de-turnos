package com.example.agendadeturnos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TurnoDao {
    @Insert
    suspend fun insertarTurno(turno: TurnoEntity)

    @Query("SELECT * FROM turnos ORDER BY id DESC")
    suspend fun obtenerTurnos(): List<TurnoEntity>
    @Query("SELECT * FROM turnos WHERE estado = 'Pendiente' ORDER BY id DESC")
    suspend fun obtenerPendientes(): List<TurnoEntity>

    @Query("SELECT * FROM turnos WHERE estado = 'Realizado' ORDER BY id DESC")
    suspend fun obtenerRealizados(): List<TurnoEntity>

    @Query("SELECT * FROM turnos WHERE estado = 'cancelado' ORDER BY id DESC")
    suspend fun obtenerCancelados(): List<TurnoEntity>

    @Query("DELETE FROM turnos WHERE id = :id")
    suspend fun borrarTurno(id: Int)
}
