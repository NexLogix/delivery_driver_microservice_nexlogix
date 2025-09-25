package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R

class VehiclesAdapter(private val items: List<Vehiculo>) : RecyclerView.Adapter<VehiclesAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.vehicle_title)
        val sub: TextView = itemView.findViewById(R.id.vehicle_sub)
        val meta: TextView = itemView.findViewById(R.id.vehicle_meta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_item, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.title.text = "${it.placa} - ${it.marcaVehiculo}"
        holder.sub.text = "${it.tipoVehiculo} · ${it.estadoVehiculo}"
        holder.meta.text = "Asignado: ${it.fechaAsignacionInicio ?: "-"}"
    }

    override fun getItemCount(): Int = items.size
}
