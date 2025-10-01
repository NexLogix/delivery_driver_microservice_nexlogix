package com.example.springboot_sena_kotlin1.Repository.ReportesInternos

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.dao.EmptyResultDataAccessException
import com.example.springboot_sena_kotlin1.models.Classes.ReportesInternos.ReportesInternos
import com.example.springboot_sena_kotlin1.models.Classes.ReportesInternos.CategoriaReportes
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.CrearReporteInternoDTO
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesInternos.EditarReporteInternoDTO
import java.time.LocalDateTime

@Repository
class ReportesInternosRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    
    // ===== CRUD REPORTES INTERNOS =====
    
    fun crearReporte(dto: CrearReporteInternoDTO, idConductor: Long): Boolean {
        return try {
            val sql = "INSERT INTO reportesConductores (idCategoriaReportes, descripcion, idConductor) VALUES (?, ?, ?)"
            val result = jdbcTemplate.update(sql, dto.idCategoriaReportes, dto.descripcion, idConductor)
            result > 0
        } catch (e: Exception) {
            println("ERROR: Excepción al crear reporte interno: ${e.message}")
            false
        }
    }
    
    fun obtenerReportesPorConductor(idConductor: Long): List<ReportesInternos> {
        val sql = """
            SELECT r.idReporte, r.idCategoriaReportes, r.descripcion, r.fechaCreacion, r.idConductor
            FROM reportesConductores r
            WHERE r.idConductor = ?
            ORDER BY r.fechaCreacion DESC
        """
        return jdbcTemplate.query(sql, arrayOf(idConductor)) { rs, _ ->
            ReportesInternos(
                idReporte = rs.getLong("idReporte"),
                idCategoriaReportes = rs.getLong("idCategoriaReportes"),
                descripcion = rs.getString("descripcion"),
                fechaCreacion = rs.getTimestamp("fechaCreacion")?.toLocalDateTime(),
                idConductor = rs.getLong("idConductor")
            )
        }
    }
    
    fun obtenerReportePorId(idReporte: Long, idConductor: Long): ReportesInternos? {
        return try {
            val sql = """
                SELECT r.idReporte, r.idCategoriaReportes, r.descripcion, r.fechaCreacion, r.idConductor
                FROM reportesConductores r
                WHERE r.idReporte = ? AND r.idConductor = ?
            """
            jdbcTemplate.queryForObject(sql, arrayOf(idReporte, idConductor)) { rs, _ ->
                ReportesInternos(
                    idReporte = rs.getLong("idReporte"),
                    idCategoriaReportes = rs.getLong("idCategoriaReportes"),
                    descripcion = rs.getString("descripcion"),
                    fechaCreacion = rs.getTimestamp("fechaCreacion")?.toLocalDateTime(),
                    idConductor = rs.getLong("idConductor")
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
    
    fun actualizarReporte(idReporte: Long, dto: EditarReporteInternoDTO, idConductor: Long): Boolean {
        return try {
            val updates = mutableListOf<String>()
            val params = mutableListOf<Any?>()
            
            dto.idCategoriaReportes?.let {
                updates.add("idCategoriaReportes = ?")
                params.add(it)
            }
            
            dto.descripcion?.let {
                updates.add("descripcion = ?")
                params.add(it)
            }
            
            if (updates.isEmpty()) return false
            
            params.add(idReporte)
            params.add(idConductor)
            
            val sql = "UPDATE reportesConductores SET ${updates.joinToString(", ")} WHERE idReporte = ? AND idConductor = ?"
            val result = jdbcTemplate.update(sql, *params.toTypedArray())
            result > 0
        } catch (e: Exception) {
            println("ERROR: Excepción al actualizar reporte: ${e.message}")
            false
        }
    }
    
    fun eliminarReporte(idReporte: Long, idConductor: Long): Boolean {
        return try {
            val sql = "DELETE FROM reportesConductores WHERE idReporte = ? AND idConductor = ?"
            val result = jdbcTemplate.update(sql, idReporte, idConductor)
            result > 0
        } catch (e: Exception) {
            println("ERROR: Excepción al eliminar reporte: ${e.message}")
            false
        }
    }
    
    // ===== CRUD CATEGORÍAS REPORTES =====
    
    fun obtenerTodasLasCategorias(): List<CategoriaReportes> {
        val sql = "SELECT idcategoria, nombreCategoria FROM categoriaReportes ORDER BY nombreCategoria"
        return jdbcTemplate.query(sql) { rs, _ ->
            CategoriaReportes(
                idcategoria = rs.getLong("idcategoria"),
                nombreCategoria = rs.getString("nombreCategoria")
            )
        }
    }
    
    // Función para obtener el nombre de la categoría (útil para el response DTO)
    fun obtenerNombreCategoria(idCategoria: Long): String? {
        return try {
            val sql = "SELECT nombreCategoria FROM categoriaReportes WHERE idcategoria = ?"
            jdbcTemplate.queryForObject(sql, arrayOf(idCategoria), String::class.java)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
}