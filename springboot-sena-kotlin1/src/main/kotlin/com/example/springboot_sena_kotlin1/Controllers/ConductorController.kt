package com.example.springboot_sena_kotlin1.Controllers

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import com.example.springboot_sena_kotlin1.Services.ConductorService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.example.springboot_sena_kotlin1.Config.UtilJWT

@RestController
@RequestMapping("/conductores_nexlogix")
class ConductorController(
    private val conductorService: ConductorService
)
{
    private val log = LoggerFactory.getLogger(ConductorController::class.java)

    @GetMapping("/information")
    fun obtenerMiPerfil(@RequestHeader(value = "Authorization", required = false) authHeader: String?): ResponseEntity<Any> {
        val (status, body) = conductorService.obtenerPerfilAutenticado(authHeader)
        return ResponseEntity.status(status).body(body)
    }
}
