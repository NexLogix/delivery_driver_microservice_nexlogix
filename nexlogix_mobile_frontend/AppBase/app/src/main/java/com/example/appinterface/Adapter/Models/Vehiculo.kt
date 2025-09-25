package com.example.appinterface.Adapter.Models

import com.google.gson.annotations.SerializedName

data class Vehiculo(
    @SerializedName("documentoIdentidad") val documentoIdentidad: String,
    @SerializedName("email") val email: String,
    @SerializedName("placa") val placa: String,
    @SerializedName("marcaVehiculo") val marcaVehiculo: String,
    @SerializedName("tipoVehiculo") val tipoVehiculo: String,
    @SerializedName("capacidad") val capacidad: Int,
    @SerializedName("estadoVehiculo") val estadoVehiculo: String,
    @SerializedName("ultimoMantenimiento") val ultimoMantenimiento: String?,
    @SerializedName("fechaAsignacionInicio") val fechaAsignacionInicio: String?,
    @SerializedName("fechaEntregaVehiculo") val fechaEntregaVehiculo: String?
)