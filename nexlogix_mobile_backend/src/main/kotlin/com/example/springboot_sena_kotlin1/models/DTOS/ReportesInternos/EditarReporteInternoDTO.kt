package com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos

data class EditarReporteInternoDTO(
    val idCategoriaReportes: Long?,
    val descripcion: String?
    // Solo campos editables, idConductor no se cambia
)