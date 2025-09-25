package com.example.appinterface.Api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import com.example.appinterface.Adapter.Conductor

data class AuthRequest(val email: String, val contrasena: String)
data class AuthResponse(val token: String?)
data class LogoutResponse(val message: String)

interface ApiServicesKotlin {
    @POST("auth/login")
    fun login(@Body req: AuthRequest): Call<AuthResponse>

    @POST("auth/logout")
    fun logout(@Header("Authorization") authorization: String): Call<LogoutResponse>

    // Endpoint para obtener la información del conductor autenticado
    @GET("conductores_nexlogix/information")
    fun getConductorInfo(@Header("Authorization") authorization: String): Call<Conductor>

    // Endpoint para obtener vehículos asignados (requiere JWT en header)
    @GET("vehiculos_asignados")
    fun getVehiculosAsignados(@Header("Authorization") authorization: String): Call<List<com.example.appinterface.Adapter.Vehiculo>>

    // Endpoint para obtener rutas asignadas (requiere JWT en header)
    @GET("rutas_asignadas")
    fun getRutasAsignadas(@Header("Authorization") authorization: String): Call<List<com.example.appinterface.Adapter.Ruta>>
}