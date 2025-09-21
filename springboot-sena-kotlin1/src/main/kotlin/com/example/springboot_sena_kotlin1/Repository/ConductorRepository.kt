package com.example.springboot_sena_kotlin1.Repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ConductorRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    /*fun crearConductor(dto: CrearConductorDTO, hashedPassword: String): Int {
        val sql = """
            INSERT INTO conductores (
                role,
                c_documentoIdentidad,
                c_nombreCompleto,
                c_email,
                c_numContacto,
                c_direccionResidencia,
                licencia,
                tipoLicencia,
                vigenciaLicencia,
                contrasena,
                idEstadoConductor,
                idestado_Usuario_control_indentidades,
                id_vehiculo_asignado,
                id_ruta_asignada
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

        val sqlDate = java.sql.Date.valueOf(dto.vigenciaLicencia)

        return jdbcTemplate.update(
            sql,
            "conductor",
            null, // c_documentoIdentidad not included in CrearConductorDTO (if needed add it)
            dto.c_nombreCompleto,
            dto.c_email,
            dto.c_numContacto,
            dto.c_direccionResidencia,
            dto.licencia,
            dto.tipoLicencia,
            sqlDate,
            hashedPassword,
            dto.idestadoUsuarioControlIndentidades,
            null,
            null
        )
    }

    fun editarConductor(dto: UpdateConductorDTO, id: String): Int {
        val sql = """
        UPDATE conductores
        SET
            c_nombreCompleto = COALESCE(?, c_nombreCompleto),
            c_email = COALESCE(?, c_email),
            c_numContacto = COALESCE(?, c_numContacto),
            c_direccionResidencia = COALESCE(?, c_direccionResidencia),
            licencia = COALESCE(?, licencia),
            tipoLicencia = COALESCE(?, tipoLicencia),
            vigenciaLicencia = COALESCE(?, vigenciaLicencia),
            idEstadoConductor = COALESCE(?, idEstadoConductor),
            idestado_Usuario_control_indentidades = COALESCE(?, idestado_Usuario_control_indentidades),
            id_vehiculo_asignado = COALESCE(?, id_vehiculo_asignado),
            id_ruta_asignada = COALESCE(?, id_ruta_asignada)
        WHERE c_email = ? OR c_documentoIdentidad = ?
    """

        val sqlDate = dto.vigenciaLicencia?.let { java.sql.Date.valueOf(it) }

        return jdbcTemplate.update(
            sql,
            dto.c_nombreCompleto,
            dto.c_email,
            dto.c_numContacto,
            dto.c_direccionResidencia,
            dto.licencia,
            dto.tipoLicencia,
            sqlDate,
            dto.idEstadoConductor,
            dto.idEstadoUsuarioControlIndentidades,
            dto.idVehiculoAsignado,
            dto.idRutaAsignada,
            id,
            id
        )
    }*/
}
