package com.example.springboot_sena_kotlin1.Services

import org.springframework.stereotype.Service
import com.example.springboot_sena_kotlin1.Repository.AuthLoginRepository
import com.example.springboot_sena_kotlin1.models.DTOS.Auth.AuthLoginDTO

@Service
class AuthLoginService(
    private val authLoginRepository: AuthLoginRepository
) {
    /**
     * Intenta autenticar usando el DTO y devuelve un JWT si las credenciales son válidas.
     * Retorna null si la autenticación falla.
     */
    fun login(dto: AuthLoginDTO): String? {
        // Prioriza login por email; si no contiene '@' intenta por documento
        val identifier = dto.email
        return authLoginRepository.login(identifier, dto.contrasena)
    }

    /**
     * Alternativa explícita: login por email
     */
    fun loginByEmail(email: String, contrasena: String): String? {
        return authLoginRepository.loginByEmail(email, contrasena)
    }
}