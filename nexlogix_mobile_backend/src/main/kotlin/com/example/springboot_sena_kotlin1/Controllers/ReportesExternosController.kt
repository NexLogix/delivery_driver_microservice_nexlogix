package com.example.springboot_sena_kotlin1.Controllers

import com.example.springboot_sena_kotlin1.Services.ReporteExternoService
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesExternos.ReporteExternoDTO
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate

@RestController
@RequestMapping("/reportes_externos")
class ReportesExternosController(
    private val reporteExternoService: ReporteExternoService,
    private val jdbcTemplate: JdbcTemplate
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
    
    // Endpoint temporal para debug - ver qué emails existen
    @GetMapping("/debug/conductores")
    fun listarConductores(): ResponseEntity<List<String>> {
        return try {
            val emails = jdbcTemplate.queryForList(
                "SELECT c_email FROM conductores LIMIT 10", 
                String::class.java
            )
            ResponseEntity.ok(emails)
        } catch (e: Exception) {
            ResponseEntity.ok(listOf("Error: ${e.message}"))
        }
    }
    
    // Endpoint temporal para crear un conductor de prueba
    @PostMapping("/debug/crear-conductor")
    fun crearConductorPrueba(): ResponseEntity<String> {
        return try {
            jdbcTemplate.update(
                "INSERT INTO conductores (c_email, c_nombre) VALUES (?, ?) ON DUPLICATE KEY UPDATE c_nombre = VALUES(c_nombre)",
                "josenorbert1959@gmail.com",
                "Jose Norbert Test"
            )
            ResponseEntity.ok("Conductor de prueba creado")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Error: ${e.message}")
        }
    }
}