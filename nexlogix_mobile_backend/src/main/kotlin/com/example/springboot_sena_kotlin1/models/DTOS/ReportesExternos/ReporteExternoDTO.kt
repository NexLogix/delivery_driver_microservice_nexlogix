package com.example.springboot_sena_kotlin1.models.DTOS.ReportesExternos

data class ReporteExternoDTO (
    val idcategoriaReportes: Long, // siempre 1
    val descripcion: String,
    val idusuarios: String // Email del usuario
)