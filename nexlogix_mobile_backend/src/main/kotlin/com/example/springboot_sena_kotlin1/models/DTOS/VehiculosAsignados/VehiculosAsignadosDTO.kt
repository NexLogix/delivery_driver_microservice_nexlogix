package com.example.springboot_sena_kotlin1.models.DTOS.VehiculosAsignados

import java.time.LocalDateTime

data class VehiculosAsignadosDTO(
    val documentoIdentidad: String,
    val email: String,
    val placa: String,
    val marcaVehiculo: String,
    val tipoVehiculo: String,
    val capacidad: Int,
    val estadoVehiculo: String,
    val ultimoMantenimiento: LocalDateTime?,
    val fechaAsignacionInicio: LocalDateTime?,
    val fechaEntregaVehiculo: LocalDateTime?
)

