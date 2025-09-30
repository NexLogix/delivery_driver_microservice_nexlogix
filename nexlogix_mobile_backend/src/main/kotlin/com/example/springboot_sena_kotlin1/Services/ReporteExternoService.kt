package com.example.springboot_sena_kotlin1.Services

import com.example.springboot_sena_kotlin1.Repository.ReportesExternosRepository
import org.springframework.stereotype.Service

@Service
class ReporteExternoService(
    private val reportesExternosRepository: ReportesExternosRepository
) {
    fun crearReporteExterno(email: String, descripcion: String): Boolean {
        return reportesExternosRepository.crearReporteExterno(email, descripcion)
    }
}