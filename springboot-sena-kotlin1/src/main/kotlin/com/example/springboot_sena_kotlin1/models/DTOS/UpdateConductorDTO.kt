package com.example.springboot_sena_kotlin1.models.DTOS

import java.time.LocalDate

data class UpdateConductorDTO(
    val c_nombreCompleto: String,
    val c_email: String,
    val c_numContacto: String,
    val c_direccionResidencia: String,
    val licencia: String?,
    val tipoLicencia: String?,
    val vigenciaLicencia: LocalDate?,
    val idEstadoConductor: Long?,
    val idEstadoUsuarioControlIndentidades: Long?,
    val idVehiculoAsignado: Long?,
    val idRutaAsignada: Long?
)
