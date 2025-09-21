package com.example.springboot_sena_kotlin1.Controllers

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.springboot_sena_kotlin1.Services.AuthLoginService
import com.example.springboot_sena_kotlin1.models.DTOS.Auth.AuthLoginDTO
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authLoginService: AuthLoginService,
    private val env: Environment // inyectar Environment para diagnóstico
) {
    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@RequestBody dto: AuthLoginDTO): ResponseEntity<Any> {
        return try {
            val token = authLoginService.login(dto)
            if (token != null) {
                ResponseEntity.ok(mapOf("token" to token))
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("error" to "Credenciales inválidas"))
            }
        } catch (ex: Exception) {
            log.error("Error en /auth/login", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error interno: ${ex.message}"))
        }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader(value = "Authorization", required = false) authHeader: String?): ResponseEntity<Any> {
        // Clear authentication on server side
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok(mapOf("message" to "Logout exitoso"))
    }
}
