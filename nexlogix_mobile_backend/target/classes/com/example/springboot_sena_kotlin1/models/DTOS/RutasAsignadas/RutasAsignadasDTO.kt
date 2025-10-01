package com.example.springboot_sena_kotlin1.models.DTOS.RutasAsignadas

import java.time.LocalDateTime

data class RutasAsignadasDTO(
    val documentoIdentidad: String,
    val email: String,
    val placa: String,
    val marcaVehiculo: String,
    val tipoVehiculo: String,
    val capacidad: Int,
    val estadoVehiculo: String,
    val fechaAsignacionInicio: LocalDateTime,
    val fechaAsignacionFinalizacion: LocalDateTime,
    val nombreRuta: String,
    val horaInicioRuta: String,
    val horaFinalizacionRuta: String,
    val estadoRuta: String,
    val descripcion: String
)

