package com.example.springboot_sena_kotlin1.models.Classes.Conductores

data class Conductor (
    val id: Long?,
    val role: String = "conductor",
    val c_documentoIdentidad: String,
    val c_nombreCompleto: String,
    val c_email: String,
    val c_numContacto: String,
    val c_direccionResidencia: String,
    val licencia: String?,
    val tipoLicencia: String?,
    val vigenciaLicencia: java.sql.Date?,
    val contrasena: String,
    val idEstadoConductor: Long? = 1, // por defecto activo
    val idVehiculoAsignada: Long?,
    val idRutaAsignado: Long?
) {
}