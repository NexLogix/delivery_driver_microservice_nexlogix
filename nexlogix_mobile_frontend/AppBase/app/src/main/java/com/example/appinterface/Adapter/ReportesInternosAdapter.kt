package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.Models.ReporteInterno
import com.example.appinterface.R

class ReportesInternosAdapter(
    private var reportes: MutableList<ReporteInterno>,
    private val onEditClick: (ReporteInterno) -> Unit,
    private val onDeleteClick: (ReporteInterno) -> Unit
) : RecyclerView.Adapter<ReportesInternosAdapter.ReporteViewHolder>() {

    class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoria: TextView = itemView.findViewById(R.id.tv_categoria)
        val descripcion: TextView = itemView.findViewById(R.id.tv_descripcion)
        val fecha: TextView = itemView.findViewById(R.id.tv_fecha)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btn_editar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btn_eliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reporte_interno, parent, false)
        return ReporteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        
        holder.categoria.text = reporte.categoria
        holder.descripcion.text = reporte.descripcion
        holder.fecha.text = reporte.fechaCreacion
        
        holder.btnEditar.setOnClickListener { onEditClick(reporte) }
        holder.btnEliminar.setOnClickListener { onDeleteClick(reporte) }
    }

    override fun getItemCount(): Int = reportes.size

    fun updateReportes(nuevosReportes: MutableList<ReporteInterno>) {
        reportes = nuevosReportes
        notifyDataSetChanged()
    }

    fun addReporte(reporte: ReporteInterno) {
        reportes.add(reporte)
        notifyItemInserted(reportes.size - 1)
    }

    fun removeReporte(reporte: ReporteInterno) {
        val index = reportes.indexOf(reporte)
        if (index != -1) {
            reportes.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateReporte(updatedReporte: ReporteInterno) {
        val index = reportes.indexOfFirst { it.id == updatedReporte.id }
        if (index != -1) {
            reportes[index] = updatedReporte
            notifyItemChanged(index)
        }
    }
}