package com.example.springboot_sena_kotlin1.models.DTOS.Auth

data class AuthLoginDTO(
    var email: String,
    var contrasena: String,
    var token: String? = null
)
