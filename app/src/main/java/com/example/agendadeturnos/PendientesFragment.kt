package com.example.agendadeturnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.appcompat.app.AlertDialog

class PendientesFragment : Fragment() {

    private lateinit var adapter: TurnoAdapter
    private lateinit var turnoDao: TurnoDao
    private lateinit var recycler: RecyclerView
    private var listaTurnos: MutableList<Turno> = mutableListOf()

    override fun onResume() {
        super.onResume()
        cargarPendientes()   // o cargarRealizados / cargarCancelados
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_turnos, container, false)

        recycler = view.findViewById(R.id.recyclerTurnos)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val db = DatabaseProvider.getDatabase(requireContext())
        turnoDao = db.turnoDao()

        cargarPendientes()

        return view
    }

    private fun cargarPendientes() {
        CoroutineScope(Dispatchers.IO).launch {
            val listaEntity = turnoDao.obtenerPendientes()

            listaTurnos = listaEntity.map {
                // Asegúrate de que Turno tenga también el id si lo necesitas
                Turno(it.cliente, it.servicio, it.fechaHora, it.estado, it.id)
            }.toMutableList()

            requireActivity().runOnUiThread {
                adapter = TurnoAdapter(listaTurnos) { turno, pos -> confirmarYEliminar(turno, pos)
                }
                recycler.adapter = adapter
            }
        }
    }

    private fun confirmarYEliminar(turno: Turno, pos: Int) {
        // Opcional: diálogo de confirmación
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar turno")
            .setMessage("¿Seguro que quieres eliminar este turno?")
            .setPositiveButton("Sí") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    turnoDao.borrarTurno(turno.id)

                    requireActivity().runOnUiThread {
                        adapter.eliminarItem(pos)
                        cargarPendientes()
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                adapter.notifyItemChanged(pos) // restaurar visualmente
            }
            .show()
    }
}
