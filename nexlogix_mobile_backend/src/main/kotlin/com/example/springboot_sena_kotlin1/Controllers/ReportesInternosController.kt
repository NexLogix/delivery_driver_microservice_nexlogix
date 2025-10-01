package com.example.springboot_sena_kotlin1.Controllers

import com.example.springboot_sena_kotlin1.Services.ReportesInternosService
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.CrearReporteInternoDTO
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.EditarReporteInternoDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reportes_internos")
class ReportesInternosController(
    private val reportesInternosService: ReportesInternosService
) {

    // ===== REPORTES INTERNOS ENDPOINTS =====

    @PostMapping
    fun crearReporte(
        @RequestHeader(value = "Authorization", required = false) authHeader: String?,
        @RequestBody dto: CrearReporteInternoDTO
    ): ResponseEntity<Any> {
        val (status, body) = reportesInternosService.crearReporte(authHeader, dto)
        return ResponseEntity.status(status).body(body)
    }

    @GetMapping
    fun obtenerMisReportes(
        @RequestHeader(value = "Authorization", required = false) authHeader: String?
    ): ResponseEntity<Any> {
        val (status, body) = reportesInternosService.obtenerMisReportes(authHeader)
        return ResponseEntity.status(status).body(body)
    }

    @GetMapping("/{idReporte}")
    fun obtenerReportePorId(
        @RequestHeader(value = "Authorization", required = false) authHeader: String?,
        @PathVariable idReporte: Long
    ): ResponseEntity<Any> {
        val (status, body) = reportesInternosService.obtenerReportePorId(authHeader, idReporte)
        return ResponseEntity.status(status).body(body)
    }

    @PatchMapping("/{idReporte}")
    fun actualizarReporte(
        @RequestHeader(value = "Authorization", required = false) authHeader: String?,
        @PathVariable idReporte: Long,
        @RequestBody dto: EditarReporteInternoDTO
    ): ResponseEntity<Any> {
        val (status, body) = reportesInternosService.actualizarReporte(authHeader, idReporte, dto)
        return ResponseEntity.status(status).body(body)
    }

    @DeleteMapping("/{idReporte}")
    fun eliminarReporte(
        @RequestHeader(value = "Authorization", required = false) authHeader: String?,
        @PathVariable idReporte: Long
    ): ResponseEntity<Any> {
        val (status, body) = reportesInternosService.eliminarReporte(authHeader, idReporte)
        return ResponseEntity.status(status).body(body)
    }

    // ===== CATEGORÍAS REPORTES  ENPOINTS =====

    @GetMapping("/categorias_reportes")
    fun obtenerCategorias(): ResponseEntity<Any> {
        val (status, body) = reportesInternosService.obtenerCategorias()
        return ResponseEntity.status(status).body(body)
    }
}