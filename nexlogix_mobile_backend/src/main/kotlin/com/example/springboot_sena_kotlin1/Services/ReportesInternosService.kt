package com.example.springboot_sena_kotlin1.Services

import com.example.springboot_sena_kotlin1.Repository.ReportesInternos.ReportesInternosRepository
import com.example.springboot_sena_kotlin1.models.Classes.ReportesInternos.ReportesInternos
import com.example.springboot_sena_kotlin1.models.Classes.ReportesInternos.CategoriaReportes
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.CrearReporteInternoDTO
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.EditarReporteInternoDTO
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.CategoriaReportesDTO
import com.example.springboot_sena_kotlin1.Config.UtilJWT
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus

@Service
class ReportesInternosService(
    private val repository: ReportesInternosRepository,
    private val conductorService: ConductorService,
    private val utilJWT: UtilJWT
) {

    // ===== MÉTODOS PRIVADOS PARA JWT =====
    
    private fun extraerIdConductorDelToken(authHeader: String?): Pair<HttpStatus, Any> {
        val (status, body) = conductorService.obtenerPerfilAutenticado(authHeader)
        if (status != HttpStatus.OK) return status to body

        val idConductor: Long? = when (body) {
            is Map<*, *> -> {
                when {
                    body.containsKey("idConductor") -> (body["idConductor"] as? Number)?.toLong()
                    body.containsKey("conductor") -> {
                        val conductor = body["conductor"]
                        if (conductor is Map<*, *>) {
                            (conductor["idConductor"] as? Number)?.toLong()
                        } else null
                    }
                    else -> null
                }
            }
            else -> null
        }

        return if (idConductor != null) {
            HttpStatus.OK to idConductor
        } else {
            HttpStatus.UNAUTHORIZED to mapOf("error" to "No se pudo obtener el ID del conductor del token")
        }
    }

    // ===== CRUD REPORTES INTERNOS =====

    fun crearReporte(authHeader: String?, dto: CrearReporteInternoDTO): Pair<HttpStatus, Any> {
        val (status, body) = extraerIdConductorDelToken(authHeader)
        if (status != HttpStatus.OK) return status to body

        val idConductor = body as Long

        // Validar que la categoría existe
        val nombreCategoria = repository.obtenerNombreCategoria(dto.idCategoriaReportes)
        if (nombreCategoria == null) {
            return HttpStatus.BAD_REQUEST to mapOf("error" to "La categoría especificada no existe")
        }

        val creado = repository.crearReporte(dto, idConductor)
        return if (creado) {
            HttpStatus.CREATED to mapOf("message" to "Reporte creado exitosamente")
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR to mapOf("error" to "No se pudo crear el reporte")
        }
    }

    fun obtenerMisReportes(authHeader: String?): Pair<HttpStatus, Any> {
        val (status, body) = extraerIdConductorDelToken(authHeader)
        if (status != HttpStatus.OK) return status to body

        val idConductor = body as Long
        // Repository ya aplica filtros: 7 días, excluye categoría 1
        val reportes = repository.obtenerReportesPorConductor(idConductor)

        // Enriquecer con nombres de categorías
        val reportesConCategoria = reportes.map { reporte ->
            val nombreCategoria = repository.obtenerNombreCategoria(reporte.idCategoriaReportes) ?: "Sin categoría"
            mapOf(
                "idReporte" to reporte.idReporte,
                "idCategoriaReportes" to reporte.idCategoriaReportes,
                "nombreCategoria" to nombreCategoria,
                "descripcion" to reporte.descripcion,
                "fechaCreacion" to reporte.fechaCreacion,
                "idConductor" to reporte.idConductor
            )
        }

        return HttpStatus.OK to reportesConCategoria
    }

    fun obtenerReportePorId(authHeader: String?, idReporte: Long): Pair<HttpStatus, Any> {
        val (status, body) = extraerIdConductorDelToken(authHeader)
        if (status != HttpStatus.OK) return status to body

        val idConductor = body as Long
        val reporte = repository.obtenerReportePorId(idReporte, idConductor)

        if (reporte == null) {
            return HttpStatus.NOT_FOUND to mapOf("error" to "Reporte no encontrado")
        }

        val nombreCategoria = repository.obtenerNombreCategoria(reporte.idCategoriaReportes) ?: "Sin categoría"
        val reporteCompleto = mapOf(
            "idReporte" to reporte.idReporte,
            "idCategoriaReportes" to reporte.idCategoriaReportes,
            "nombreCategoria" to nombreCategoria,
            "descripcion" to reporte.descripcion,
            "fechaCreacion" to reporte.fechaCreacion,
            "idConductor" to reporte.idConductor
        )

        return HttpStatus.OK to reporteCompleto
    }

    fun actualizarReporte(authHeader: String?, idReporte: Long, dto: EditarReporteInternoDTO): Pair<HttpStatus, Any> {
        val (status, body) = extraerIdConductorDelToken(authHeader)
        if (status != HttpStatus.OK) return status to body

        val idConductor = body as Long

        // Validar que el reporte existe y pertenece al usuario
        val reporteExistente = repository.obtenerReportePorId(idReporte, idConductor)
        if (reporteExistente == null) {
            return HttpStatus.NOT_FOUND to mapOf("error" to "Reporte no encontrado")
        }

        // Validar categoría si se está actualizando
        dto.idCategoriaReportes?.let { nuevaCategoria ->
            val nombreCategoria = repository.obtenerNombreCategoria(nuevaCategoria)
            if (nombreCategoria == null) {
                return HttpStatus.BAD_REQUEST to mapOf("error" to "La categoría especificada no existe")
            }
        }

        val actualizado = repository.actualizarReporte(idReporte, dto, idConductor)
        return if (actualizado) {
            HttpStatus.OK to mapOf("message" to "Reporte actualizado exitosamente")
        } else {
            HttpStatus.FORBIDDEN to mapOf("error" to "No se puede editar el reporte. Solo se pueden editar reportes creados en las últimas 24 horas")
        }
    }

    fun eliminarReporte(authHeader: String?, idReporte: Long): Pair<HttpStatus, Any> {
        val (status, body) = extraerIdConductorDelToken(authHeader)
        if (status != HttpStatus.OK) return status to body

        val idConductor = body as Long

        // Validar que el reporte existe y pertenece al usuario
        val reporteExistente = repository.obtenerReportePorId(idReporte, idConductor)
        if (reporteExistente == null) {
            return HttpStatus.NOT_FOUND to mapOf("error" to "Reporte no encontrado")
        }

        val eliminado = repository.eliminarReporte(idReporte, idConductor)
        return if (eliminado) {
            HttpStatus.OK to mapOf("message" to "Reporte eliminado exitosamente")
        } else {
            HttpStatus.FORBIDDEN to mapOf("error" to "No se puede eliminar el reporte. Solo se pueden eliminar reportes creados en las últimas 24 horas")
        }
    }

    // ===== CATEGORÍAS REPORTES =====

    fun obtenerCategorias(): Pair<HttpStatus, Any> {
        val categorias = repository.obtenerTodasLasCategorias()
        val categoriasDTO = categorias.map { categoria ->
            CategoriaReportesDTO(
                idcategoria = categoria.idcategoria,
                nombreCategoria = categoria.nombreCategoria
            )
        }
        return HttpStatus.OK to categoriasDTO
    }
}