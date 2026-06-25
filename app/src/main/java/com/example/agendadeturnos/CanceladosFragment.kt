package com.example.agendadeturnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CanceladosFragment : Fragment() {

    private lateinit var adapter: TurnoAdapter
    private lateinit var turnoDao: TurnoDao
    private lateinit var listaTurnos: MutableList<Turno>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_turnos, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerTurnos)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val db = DatabaseProvider.getDatabase(requireContext())
        turnoDao = db.turnoDao()

        cargarCancelados(recycler)

        return view
    }

    private fun cargarCancelados(recycler: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            val listaEntity = turnoDao.obtenerCancelados()

            listaTurnos = listaEntity.map {
                Turno(it.cliente, it.servicio, it.fechaHora, it.estado, it.id)
            }.toMutableList()

            requireActivity().runOnUiThread {
                adapter = TurnoAdapter(listaTurnos) { turno, pos ->
                    confirmarYEliminar(turno, pos)
                }
                recycler.adapter = adapter
            }
        }
    }

    private fun confirmarYEliminar(turno: Turno, pos: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar turno")
            .setMessage("¿Seguro que quieres eliminar este turno?")
            .setPositiveButton("Sí") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    turnoDao.borrarTurno(turno.id)

                    requireActivity().runOnUiThread {
                        adapter.eliminarItem(pos)
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                adapter.notifyItemChanged(pos)
            }
            .show()
    }
}
