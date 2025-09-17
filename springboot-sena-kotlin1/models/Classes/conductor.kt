package com.example.springboot_sena_kotlin1.models.Classes

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException

@Repository
class ConductorRepository(val jdbcTemplate: JdbcTemplate) {

    fun crearConductor(dto: conductor): Int {
        val sql = """
            INSERT INTO conductores (
                role, c_documentoIdentidad, c_nombreCompleto, c_email, c_numContacto, 
                c_direccionResidencia, licencia, tipoLicencia, vigenciaLicencia, contrasena, 
                idEstadoConductor, id_vehiculo_asignado, id_ruta_asignada
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """
        return jdbcTemplate.update(
            sql,
            dto.role,
            dto.c_documentoIdentidad,
            dto.c_nombreCompleto,
            dto.c_email,
            dto.c_numContacto,
            dto.c_direccionResidencia,
            dto.licencia,
            dto.tipoLicencia,
            dto.vigenciaLicencia,
            dto.contrasena,
            dto.idEstadoConductor,
            dto.idVehiculoAsignada,
            dto.idRutaAsignado
        )
    }

    fun buscarConductorPorDocumento(documento: String): conductor? {
        val sql = "SELECT * FROM conductores WHERE c_documentoIdentidad = ?"
        return jdbcTemplate.query(sql, ConductorRowMapper(), documento).firstOrNull()
    }

    fun buscarConductorPorEmail(email: String): conductor? {
        val sql = "SELECT * FROM conductores WHERE c_email = ?"
        return jdbcTemplate.query(sql, ConductorRowMapper(), email).firstOrNull()
    }

    fun editarConductor(dto: conductor, id: String): Int {
        val sql = """
            UPDATE conductores c
            SET 
                c.c_nombreCompleto = COALESCE(?, c.c_nombreCompleto),
                c.c_numContacto = COALESCE(?, c.c_numContacto),
                c.c_direccionResidencia = COALESCE(?, c.c_direccionResidencia),
                c.licencia = COALESCE(?, c.licencia),
                c.tipoLicencia = COALESCE(?, c.tipoLicencia),
                c.vigenciaLicencia = COALESCE(?, c.vigenciaLicencia),
                c.idEstadoConductor = COALESCE(?, c.idEstadoConductor)
            WHERE c.c_email = ? OR c.c_documentoIdentidad = ?
        """
        return jdbcTemplate.update(
            sql,
            dto.c_nombreCompleto,
            dto.c_numContacto,
            dto.c_direccionResidencia,
            dto.licencia,
            dto.tipoLicencia,
            dto.vigenciaLicencia,
            dto.idEstadoConductor,
            id,
            id
        )
    }

    fun eliminarConductorPorDocumento(documento: String): Int {
        val sql = "DELETE FROM conductores WHERE c_documentoIdentidad = ?"
        return jdbcTemplate.update(sql, documento)
    }

    fun listarConductores(): List<conductor> {
        val sql = "SELECT * FROM conductores"
        return jdbcTemplate.query(sql, ConductorRowMapper())
    }
}

class ConductorRowMapper : RowMapper<conductor> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): conductor {
        return conductor(
            role = rs.getString("role"),
            c_documentoIdentidad = rs.getString("c_documentoIdentidad"),
            c_nombreCompleto = rs.getString("c_nombreCompleto"),
            c_email = rs.getString("c_email"),
            c_numContacto = rs.getString("c_numContacto"),
            c_direccionResidencia = rs.getString("c_direccionResidencia"),
            licencia = rs.getString("licencia"),
            tipoLicencia = rs.getString("tipoLicencia"),
            vigenciaLicencia = rs.getDate("vigenciaLicencia"),
            contrasena = rs.getString("contrasena"),
            idEstadoConductor = rs.getLong("idEstadoConductor"),
            idVehiculoAsignada = rs.getObject("id_vehiculo_asignado") as? Long,
            idRutaAsignado = rs.getObject("id_ruta_asignada") as? Long
        )
    }
}
