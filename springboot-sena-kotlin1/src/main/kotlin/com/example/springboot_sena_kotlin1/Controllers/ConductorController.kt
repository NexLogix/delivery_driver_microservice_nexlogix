package com.example.springboot_sena_kotlin1.Controllers

import org.springframework.web.bind.annotation.*
import com.example.springboot_sena_kotlin1.Services.ConductorService
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/conductores_nexlogix")
class ConductorController(
    private val conductorService: ConductorService
) {
    // @GetMapping

    /*@PostMapping
    fun crearConductor(@RequestBody nuevoConductor: CrearConductorDTO): ResponseEntity<String> {
        return try {
            conductorService.crearConductor(nuevoConductor)
            ResponseEntity.ok("Conductor creado correctamente")
        } catch (ex: Exception) {
            ResponseEntity.status(500).body("Error al crear conductor: ${ex.message}")
        }
    }

    @PatchMapping("/{id:.+}")
    fun actualizarParcial(
        @PathVariable id: String,
        @RequestBody dto: UpdateConductorDTO
    ): ResponseEntity<String> {
        return try {
            val rows = conductorService.editarConductor(dto, id)
            if (rows > 0) ResponseEntity.ok("Conductor actualizado correctamente")
            else ResponseEntity.status(404).body("Conductor no encontrado con ese email o documento")
        } catch (ex: Exception) {
            ResponseEntity.status(500).body("Error al actualizar conductor: ${ex.message}")
        }
    }
    *
     */

}
