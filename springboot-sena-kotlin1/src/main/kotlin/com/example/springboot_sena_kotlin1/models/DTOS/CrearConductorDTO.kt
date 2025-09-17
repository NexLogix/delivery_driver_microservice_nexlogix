package com.example.springboot_sena_kotlin1.models.DTOS

import java.time.LocalDate

data class CrearConductorDTO(
    val c_nombreCompleto: String,
    val c_email: String,
    val c_numContacto: String,
    val c_direccionResidencia: String,
    val licencia: String,
    val tipoLicencia: String, // enum('A1','A2','B1','B2','B3','C1','C2','C3')
    val vigenciaLicencia: LocalDate,
    val contrasena: String,
    val idestadoUsuarioControlIndentidades: Long
)

