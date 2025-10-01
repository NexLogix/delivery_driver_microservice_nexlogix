package com.example.springboot_sena_kotlin1.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.dao.EmptyResultDataAccessException

@Repository
class ReportesExternosRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    
    data class ConductorResult(
        val idConductor: Long,
        val email: String
    )
    
    fun buscarConductor(email: String): ConductorResult? {
        val sql = "SELECT idConductor, c_email FROM conductores WHERE c_email = ?"
        return try {
            println("DEBUG: Buscando conductor con email: $email")
            val resultado = jdbcTemplate.queryForObject(
                sql,
                arrayOf(email)
            ) { rs, _ ->
                ConductorResult(
                    idConductor = rs.getLong("idConductor"),
                    email = rs.getString("c_email")
                )
            }
            println("DEBUG: Conductor encontrado: $resultado")
            resultado
        } catch (e: EmptyResultDataAccessException) {
            println("DEBUG: No se encontró conductor con email: $email")
            null
        } catch (e: Exception) {
            println("ERROR: Excepción al buscar conductor: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun crearReporteExterno(email: String, descripcion: String): Boolean {
        return try {
            println("DEBUG: Iniciando creación de reporte para email: $email")
            val conductor = buscarConductor(email)
            if (conductor != null) {
                val sql = "INSERT INTO reportesConductores (idCategoriaReportes, descripcion, idConductor) VALUES (?, ?, ?)"
                val result = jdbcTemplate.update(
                    sql,
                    1, // idCategoriaReportes siempre 1
                    descripcion,
                    conductor.idConductor
                )
                println("DEBUG: Reporte creado exitosamente, filas afectadas: $result")
                result > 0
            } else {
                println("DEBUG: No se pudo crear reporte, conductor no encontrado")
                false
            }
        } catch (e: Exception) {
            println("ERROR: Excepción al crear reporte: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}