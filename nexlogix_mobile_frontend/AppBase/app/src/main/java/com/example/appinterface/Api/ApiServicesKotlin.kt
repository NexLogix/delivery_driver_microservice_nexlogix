package com.example.appinterface.Api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Header
import okhttp3.ResponseBody
import com.example.appinterface.Adapter.Models.Conductor
import com.example.appinterface.Adapter.Models.Ruta
import com.example.appinterface.Adapter.Models.Vehiculo
import com.example.appinterface.Adapter.Models.ReporteExterno
import com.example.appinterface.Adapter.Models.ReporteInterno

data class AuthRequest(val email: String, val contrasena: String)
data class AuthResponse(val token: String?)
data class LogoutResponse(val message: String)
data class CategoriaReporte(val id: Int, val nombre: String)
data class ReporteInternoRequest(val idCategoriaReportes: Int, val descripcion: String)
data class ReporteInternoResponse(val id: Int, val idCategoriaReportes: Int, val descripcion: String, val fechaCreacion: String)

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
    fun getVehiculosAsignados(@Header("Authorization") authorization: String): Call<List<Vehiculo>>

    // Endpoint para obtener rutas asignadas (requiere JWT en header)
    @GET("rutas_asignadas")
    fun getRutasAsignadas(@Header("Authorization") authorization: String): Call<List<Ruta>>

    // Endpoint para enviar reporte externo (NO requiere JWT, es público)
    @POST("reportes_externos")
    fun enviarReporteExterno(@Body reporte: ReporteExterno): Call<ResponseBody>

    // Endpoints para reportes internos (requieren JWT)
    @GET("reportes_internos")
    fun getReportesInternos(@Header("Authorization") authorization: String): Call<List<ReporteInternoResponse>>

    @POST("reportes_internos")
    fun crearReporteInterno(@Header("Authorization") authorization: String, @Body reporte: ReporteInternoRequest): Call<ReporteInternoResponse>

    @PATCH("reportes_internos/{id}")
    fun actualizarReporteInterno(@Header("Authorization") authorization: String, @Path("id") id: Int, @Body reporte: ReporteInternoRequest): Call<ReporteInternoResponse>

    @DELETE("reportes_internos/{id}")
    fun eliminarReporteInterno(@Header("Authorization") authorization: String, @Path("id") id: Int): Call<ResponseBody>

    @GET("reportes_internos/categorias_reportes")
    fun getCategoriasReportes(@Header("Authorization") authorization: String): Call<List<CategoriaReporte>>
}