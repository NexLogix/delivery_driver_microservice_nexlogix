package com.example.springboot_sena_kotlin1.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.dao.EmptyResultDataAccessException
import com.example.springboot_sena_kotlin1.models.DTOS.ReportesExternos.BuscarConductorReporteExternoDTO

@Repository
class ReportesExternosRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun buscarConductor(email: String): BuscarConductorReporteExternoDTO? {
        val sql = "SELECT idConductor, c_email FROM conductores WHERE c_email = ?"
        return try {
            jdbcTemplate.queryForObject(
                sql,
                arrayOf(email)
            ) { rs, _ ->
                BuscarConductorReporteExternoDTO(
                    idConductor = rs.getLong("idConductor"),
                    email = rs.getString("c_email")
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun crearReporteExterno(email: String, descripcion: String): Boolean {
        val conductor = buscarConductor(email)
        if (conductor != null) {
            val sql = "INSERT INTO reportes_externos (idcategoriaReportes, descripcion, idusuarios) VALUES (?, ?, ?)"
            return jdbcTemplate.update(
                sql,
                1, // idcategoriaReportes siempre 1
                descripcion,
                conductor.idConductor
            ) > 0
        }
        return false
    }
}