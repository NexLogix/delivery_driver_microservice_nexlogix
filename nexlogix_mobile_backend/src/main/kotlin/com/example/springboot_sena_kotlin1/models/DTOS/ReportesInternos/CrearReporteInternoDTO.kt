package com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos

data class CrearReporteInternoDTO(
    val idCategoriaReportes: Long,
    val descripcion: String?
    // idConductor se obtiene automáticamente del JWT
)