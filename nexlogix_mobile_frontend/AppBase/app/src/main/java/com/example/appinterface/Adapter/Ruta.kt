package com.example.appinterface.Adapter

import com.google.gson.annotations.SerializedName

data class Ruta(
    @SerializedName("documentoIdentidad") val documentoIdentidad: String,
    @SerializedName("email") val email: String,
    @SerializedName("placa") val placa: String,
    @SerializedName("marcaVehiculo") val marcaVehiculo: String,
    @SerializedName("tipoVehiculo") val tipoVehiculo: String,
    @SerializedName("capacidad") val capacidad: Int,
    @SerializedName("estadoVehiculo") val estadoVehiculo: String,
    @SerializedName("fechaAsignacionInicio") val fechaAsignacionInicio: String?,
    @SerializedName("fechaAsignacionFinalizacion") val fechaAsignacionFinalizacion: String?,
    @SerializedName("nombreRuta") val nombreRuta: String?,
    @SerializedName("horaInicioRuta") val horaInicioRuta: String?,
    @SerializedName("horaFinalizacionRuta") val horaFinalizacionRuta: String?,
    @SerializedName("estadoRuta") val estadoRuta: String?,
    @SerializedName("descripcion") val descripcion: String?
)
