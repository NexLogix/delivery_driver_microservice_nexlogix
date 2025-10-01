package com.example.appinterface.Adapter.Models

data class ReporteInterno(
    val id: Int,
    val idCategoriaReportes: Int,
    val categoria: String, // Nombre de la categoría para mostrar en UI
    val descripcion: String,
    val fechaCreacion: String
)