package com.example.agendadeturnos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

class TurnoAdapter(
    private var listaTurnos: MutableList<Turno>,
    private val onEliminar: (Turno, Int) -> Unit
) : RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_turno, parent, false)
        return TurnoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: TurnoViewHolder, position: Int) {
        val turno = listaTurnos[position]

        holder.tvCliente.text = "Cliente: ${turno.cliente}"
        holder.tvServicio.text = "Servicio: ${turno.servicio}"
        holder.tvFechaHora.text = turno.fechaHora
        holder.tvEstado.text = turno.estado

        holder.btnEliminar.setOnClickListener {
            onEliminar(turno, position)   // 👈 ahora sí usa el callback real
        }

        // Colores según estado
        when (turno.estado.lowercase()) {
            "pendiente" -> holder.tvEstado.setTextColor(Color.parseColor("#E53935")) // rojo
            "realizado" -> holder.tvEstado.setTextColor(Color.parseColor("#43A047")) // verde
            "cancelado" -> holder.tvEstado.setTextColor(Color.parseColor("#FBC02D")) // amarillo
        }
    }

    override fun getItemCount(): Int = listaTurnos.size

    fun actualizarLista(nuevaLista: MutableList<Turno>) {
        listaTurnos = nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarItem(posicion: Int) {
        if (posicion in 0 until listaTurnos.size) {
            listaTurnos.removeAt(posicion)
            notifyItemRemoved(posicion)
        }
    }

    class TurnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCliente: TextView = itemView.findViewById(R.id.tvCliente)
        val tvServicio: TextView = itemView.findViewById(R.id.tvServicio)
        val tvFechaHora: TextView = itemView.findViewById(R.id.tvFechaHora)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)
    }
}

