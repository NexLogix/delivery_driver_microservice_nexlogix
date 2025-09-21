package com.example.springboot_sena_kotlin1.Controllers

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.springboot_sena_kotlin1.Services.AuthLoginService
import com.example.springboot_sena_kotlin1.models.DTOS.Auth.AuthLoginDTO
import jakarta.annotation.PostConstruct

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authLoginService: AuthLoginService,
    private val env: Environment // inyectar Environment para diagnóstico
) {
    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostConstruct
    fun init() {
        // Loguea las propiedades principales para verificar si application.properties se cargó
        val url = env.getProperty("spring.datasource.url")
        val user = env.getProperty("spring.datasource.username")
        val port = env.getProperty("server.port")
        val profiles = env.activeProfiles.joinToString(",").ifEmpty { "default" }
        log.info("DIAGNOSTIC: spring.datasource.url = ${url ?: "NULL"}")
        log.info("DIAGNOSTIC: spring.datasource.username = ${user ?: "NULL"}")
        log.info("DIAGNOSTIC: server.port = ${port ?: "NULL"}")
        log.info("DIAGNOSTIC: active.profiles = $profiles")
    }

    @GetMapping("/dbinfo")
    fun dbInfo(): ResponseEntity<Any> {
        val info = mapOf(
            "datasource.url" to (env.getProperty("spring.datasource.url") ?: "NULL"),
            "datasource.username" to (env.getProperty("spring.datasource.username") ?: "NULL"),
            "server.port" to (env.getProperty("server.port") ?: "NULL"),
            "active.profiles" to (env.activeProfiles.joinToString(",").ifEmpty { "default" })
        )
        return ResponseEntity.ok(info)
    }

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
}
