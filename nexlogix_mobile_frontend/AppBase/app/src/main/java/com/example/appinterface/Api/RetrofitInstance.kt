package com.example.appinterface.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://dog.ceo/api/"
    // Ajusta el puerto según tu servidor local. Para backend en localhost:8081 usar 10.0.2.2 desde emulador
    const val BASE_URL_APIKOTLIN = "http://10.0.2.2:8081/"

    // Cliente HTTP con logs y timeouts
    private val httpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val api2kotlin: ApiServicesKotlin by lazy {
        // Configurar Gson con setLenient para manejar JSON menos estricto del backend
        val gson = GsonBuilder()
            .setLenient()
            .create()
            
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Convertir respuestas a objetos Kotlin
            .build()
            .create(ApiServicesKotlin::class.java)
    }
}