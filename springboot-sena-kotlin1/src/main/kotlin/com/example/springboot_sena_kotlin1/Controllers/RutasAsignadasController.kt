package com.example.springboot_sena_kotlin1.Controllers

import com.example.springboot_sena_kotlin1.Services.RutasAsignadasService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rutas_asignadas")
class RutasAsignadasController(private val service: RutasAsignadasService) {

    @GetMapping
    fun rutasAsignadas(@RequestHeader(value = "Authorization", required = false) authHeader: String?): ResponseEntity<Any> {
        val (status, body) = service.getRutasAsignadasByToken(authHeader)
        return ResponseEntity.status(status).body(body)
    }
}

