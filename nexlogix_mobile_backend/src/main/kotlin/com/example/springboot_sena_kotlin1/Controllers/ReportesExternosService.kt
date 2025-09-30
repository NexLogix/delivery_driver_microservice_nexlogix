package com.example.springboot_sena_kotlin1.Controllers

import com.example.springboot_sena_kotlin1.Services.ReporteExternoService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

data class CrearReporteRequest(
    val email: String,
    val descripcion: String
)

@RestController
@RequestMapping("/reportes_externos")
class ReportesExternosController(
    private val reporteExternoService: ReporteExternoService
) {

    @PostMapping
    fun crearReporte(@RequestBody request: CrearReporteRequest): ResponseEntity<String> {
        val creado = reporteExternoService.crearReporteExterno(request.email, request.descripcion)
        return if (creado) {
            ResponseEntity.ok("Reporte creado exitosamente")
        } else {
            ResponseEntity.badRequest().body("No se pudo crear el reporte: conductor no encontrado")
        }
    }
}