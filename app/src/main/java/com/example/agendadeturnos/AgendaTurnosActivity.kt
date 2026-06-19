package com.example.agendadeturnos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgendaTurnosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TurnoAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda_turnos)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerTurnos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar BD
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "agenda_turnos_db"
        ).build()

        // Cargar turnos desde BD
        cargarTurnos()

        // FAB para abrir formulario
        val fab = findViewById<FloatingActionButton>(R.id.fabAgregarTurno)
        fab.setOnClickListener {
            val intent = Intent(this, NuevoTurnoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarTurnos() // refrescar lista al volver del formulario
    }

    private fun cargarTurnos() {
        CoroutineScope(Dispatchers.IO).launch {
            val lista = db.turnoDao().obtenerTurnos()
            runOnUiThread {
                adapter = TurnoAdapter(lista.map {
                    Turno(it.cliente, it.servicio, it.fechaHora, it.estado)
                })
                recyclerView.adapter = adapter
            }
        }
    }
}

