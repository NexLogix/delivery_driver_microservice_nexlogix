package com.example.appinterface.Adapter.Models

import com.google.gson.annotations.SerializedName

data class Conductor(
    @SerializedName("idConductor") val idConductor: Int,
    @SerializedName("Role") val role: String,
    @SerializedName("c_documentoIdentidad") val documentoIdentidad: String,
    @SerializedName("c_email") val email: String,
    @SerializedName("c_numContacto") val numContacto: String,
    @SerializedName("c_direccionResidencia") val direccionResidencia: String,
    @SerializedName("licencia") val licencia: String,
    @SerializedName("tipoLicencia") val tipoLicencia: String,
    @SerializedName("vigenciaLicencia") val vigenciaLicencia: String,
    @SerializedName("idEstadoConductor") val idEstadoConductor: Int,
    @SerializedName("idestado_Usuario_control_indentidades") val idEstadoUsuarioControl: Int
)