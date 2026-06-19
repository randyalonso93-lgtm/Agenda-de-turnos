package com.example.agendadeturnos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar



class NuevoTurnoActivity : AppCompatActivity() {

    private lateinit var etCliente: TextInputEditText
    private lateinit var etServicio: TextInputEditText
    private lateinit var spEstado: Spinner
    private lateinit var btnFechaHora: Button
    private lateinit var btnGuardar: Button

    private var fechaHoraSeleccionada: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_turno)

        etCliente = findViewById(R.id.etCliente)
        etServicio = findViewById(R.id.etServicio)
        spEstado = findViewById(R.id.spEstado)
        btnFechaHora = findViewById(R.id.btnFechaHora)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Selección de fecha y hora
        btnFechaHora.setOnClickListener {
            val calendario = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                TimePickerDialog(this, { _, hour, minute ->
                    fechaHoraSeleccionada = "$day/${month+1}/$year - $hour:$minute"
                    btnFechaHora.text = fechaHoraSeleccionada
                }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true).show()
            }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Guardar turno
        btnGuardar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "agenda_turnos_db"
                ).build()
                db.turnoDao().insertarTurno(
                    TurnoEntity(
                        cliente = cliente,
                        servicio = servicio,
                        fechaHora = fechaHoraSeleccionada,
                        estado = estado
                    )
                )
            }

        }
    }
}
