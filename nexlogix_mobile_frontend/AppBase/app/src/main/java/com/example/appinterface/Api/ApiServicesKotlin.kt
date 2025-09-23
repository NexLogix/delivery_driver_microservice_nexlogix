package com.example.appinterface.Api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

data class AuthRequest(val email: String, val contrasena: String)
data class AuthResponse(val token: String?)
data class LogoutResponse(val message: String)

interface ApiServicesKotlin {
    @GET("usuarios")
    fun getPersonas(): Call<List<String>>

    @POST("auth/login")
    fun login(@Body req: AuthRequest): Call<AuthResponse>

    @POST("auth/logout")
    fun logout(@Header("Authorization") authorization: String): Call<LogoutResponse>
}