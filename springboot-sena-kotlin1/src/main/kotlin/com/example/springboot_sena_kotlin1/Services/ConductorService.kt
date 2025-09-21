package com.example.springboot_sena_kotlin1.Services

import com.example.springboot_sena_kotlin1.Repository.ConductorRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class ConductorService
(
    private val conductorRepository: ConductorRepository
)
    {
        private val passwordEncoder = BCryptPasswordEncoder()

        /*fun crearConductor(conduct: CrearConductorDTO)
        {
            val hashedPassword = passwordEncoder.encode(conduct.contrasena)
            conductorRepository.crearConductor(conduct, hashedPassword)
        }

        fun editarConductor(dto: UpdateConductorDTO, id: String): Int
        {
            return conductorRepository.editarConductor(dto, id)
        }*/
}
