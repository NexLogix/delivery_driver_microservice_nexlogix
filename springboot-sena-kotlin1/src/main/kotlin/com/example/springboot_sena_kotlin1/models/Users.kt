package com.example.springboot_sena_kotlin1.models

data class Users(
    val documentoIdentidad: String,
    val nombreCompleto: String,
    val email: String,
    val numContacto: String?,
    val contrasena: String,
    val direccionResidencia: String?,
    val idRole: Long,
    val idEstado: Long,
    val idPuestos: Long
)