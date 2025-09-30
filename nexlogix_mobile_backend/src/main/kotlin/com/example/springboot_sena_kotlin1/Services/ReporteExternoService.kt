package com.example.springboot_sena_kotlin1.Services

import com.example.springboot_sena_kotlin1.Repository.ReportesExternosRepository
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesExternos.ReporteExternoDTO
import org.springframework.stereotype.Service

@Service
class ReporteExternoService(
    private val reportesExternosRepository: ReportesExternosRepository
) {
    fun crearReporteExterno(email: String, descripcion: String): Boolean {
        return reportesExternosRepository.crearReporteExterno(email, descripcion)
    }
    
    fun crearReporteExternoPorDTO(dto: ReporteExternoDTO): Boolean {
        // Validar que el email no esté vacío
        if (dto.idusuarios.isBlank()) {
            return false
        }
        
        // Usar el método existente del repository
        return reportesExternosRepository.crearReporteExterno(dto.idusuarios, dto.descripcion)
    }
}