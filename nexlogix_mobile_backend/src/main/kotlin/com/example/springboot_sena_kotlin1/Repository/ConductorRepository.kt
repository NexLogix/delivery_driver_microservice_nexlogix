package com.example.springboot_sena_kotlin1.Repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.dao.EmptyResultDataAccessException

@Repository
class ConductorRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    // Nuevo: buscar conductor por identificador (email o documento)
    fun infoConductorByIdentifier(identifier: String): Map<String, Any?>? {
        val sql = """
            SELECT
                idConductor,
                Role,
                c_documentoIdentidad,
                c_email,
                c_numContacto,
                c_direccionResidencia,
                licencia,
                tipoLicencia,
                vigenciaLicencia,
                idEstadoConductor,
                idestado_Usuario_control_indentidades
            FROM conductores
            WHERE c_email = ? OR c_documentoIdentidad = ?
            LIMIT 1
        """.trimIndent()

        return try {
            jdbcTemplate.queryForMap(sql, identifier, identifier)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }
}
