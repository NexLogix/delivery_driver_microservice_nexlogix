package com.example.springboot_sena_kotlin1.Services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import com.example.springboot_sena_kotlin1.Repository.ConductorRepository
import com.example.springboot_sena_kotlin1.models.DTOS.UpdateConductorDTO

@Service
class ConductorService(
    private val conductorRepository: ConductorRepository
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun listaConductores(): List<Map<String, Any?>> {
        return conductorRepository.listaConductores()
    }

    fun buscarConductor(id: String): List<Map<String, Any?>> {
        return conductorRepository.buscarConductor(id)
    }

    fun crearConductor(conduct: com.example.springboot_sena_kotlin1.models.DTOS.CrearConductorDTO) {
        val hashedPassword = passwordEncoder.encode(conduct.contrasena)
        conductorRepository.crearConductor(conduct, hashedPassword)
    }

    fun editarConductor(dto: UpdateConductorDTO, id: String): Int  {
        return conductorRepository.editarConductor(dto, id)
    }

    // eliminar deshabilitado por petición
    // fun eliminarConductor(id: String): Int {
    //     return conductorRepository.eliminarConductor(id)
    // }
}
