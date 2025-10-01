package com.example.springboot_sena_kotlin1.Controllers

import com.example.springboot_sena_kotlin1.Services.ReporteExternoService
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesExternos.ReporteExternoDTO
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/reportes_externos")
class ReportesExternosController(
    private val reporteExternoService: ReporteExternoService
) {

    @PostMapping
    fun crearReporte(@RequestBody dto: ReporteExternoDTO): ResponseEntity<String> {
        // Validar que el email no esté vacío
        if (dto.idusuarios.isBlank()) {
            return ResponseEntity.badRequest().body("El email del usuario es requerido")
        }
        
        val creado = reporteExternoService.crearReporteExternoPorDTO(dto)
        return if (creado) {
            ResponseEntity.ok("Reporte creado exitosamente")
        } else {
            ResponseEntity.badRequest().body("No se pudo crear el reporte: usuario con email '${dto.idusuarios}' no encontrado")
        }
    }
}