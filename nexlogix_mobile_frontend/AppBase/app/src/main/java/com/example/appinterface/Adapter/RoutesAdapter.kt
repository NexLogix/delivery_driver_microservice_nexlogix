package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R

class RoutesAdapter(private val items: List<Ruta>) : RecyclerView.Adapter<RoutesAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.route_title)
        val sub: TextView = itemView.findViewById(R.id.route_sub)
        val desc: TextView = itemView.findViewById(R.id.route_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.route_item, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.title.text = "Ruta ${it.nombreRuta ?: "-"} - ${it.placa}"
        val inicio = it.fechaAsignacionInicio?.split("T")?.getOrNull(0) ?: "-"
        holder.sub.text = "Estado: ${it.estadoRuta ?: "-"} · Fecha inicio: $inicio"
        holder.desc.text = it.descripcion ?: "Sin descripción"
    }

    override fun getItemCount(): Int = items.size
}
