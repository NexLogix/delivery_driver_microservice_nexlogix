package com.example.springboot_sena_kotlin1.Controllers

import com.example.springboot_sena_kotlin1.Services.VehiculosAsignadosService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vehiculos_asignados")
class VehiculosAsignadosController(private val service: VehiculosAsignadosService) {

    @GetMapping
    fun vehiculosAsignados(@RequestHeader(value = "Authorization", required = false) authHeader: String?): ResponseEntity<Any> {
        val (status, body) = service.getVehiculosAsignadosByToken(authHeader)
        return ResponseEntity.status(status).body(body)
    }
}
