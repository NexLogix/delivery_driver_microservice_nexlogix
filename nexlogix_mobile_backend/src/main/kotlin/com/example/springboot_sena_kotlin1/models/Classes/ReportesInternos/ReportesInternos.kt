package com.example.springboot_sena_kotlin1.models.Classes.ReportesInternos

import java.time.LocalDateTime

data class ReportesInternos(
    val idReporte: Long? = null, // puede ser null porque es autoincrement
    val idCategoriaReportes: Long,
    val descripcion: String?,
    val fechaCreacion: LocalDateTime? = null, // MySQL lo pone por defecto
    val idConductor: Long
)
