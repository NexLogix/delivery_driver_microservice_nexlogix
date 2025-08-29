    package com.example.springboot_sena_kotlin1.Controllers

    import org.springframework.web.bind.annotation.*
    import com.example.springboot_sena_kotlin1.Services.UsuarioService
    import com.example.springboot_sena_kotlin1.models.Users
    import com.example.springboot_sena_kotlin1.models.UsersName
    import com.example.springboot_sena_kotlin1.models.UserUpdateDTO


    import org.springframework.http.ResponseEntity


    @RestController
    @RequestMapping("/usuarios")
    class UsuarioController(
        private val usuarioService: UsuarioService
    ) {
        @GetMapping("/detalles")
        fun listarDetalles(): List<Users> {
            return usuarioService.detallesUsuarios()
        }

        @GetMapping()
        fun listarNombres(): List<UsersName> {
            return usuarioService.listarUsuarios()
        }

        @PostMapping()
        fun crearUsuario(@RequestBody usuario: Users) {
            usuarioService.crearUsuario(usuario)
        }

        @PatchMapping("/{id:.+}")
        fun actualizarParcial(
            @PathVariable id: String,
            @RequestBody dto: UserUpdateDTO
        ): ResponseEntity<String> {
            return try {
                val rows = usuarioService.editarUsuario(dto, id)
                if (rows > 0) ResponseEntity.ok(" Usuario actualizado correctamente")
                else ResponseEntity.status(404).body("Usuario no encontrado con ese email o documento")
            } catch (ex: Exception) {
                ResponseEntity.status(500).body("Error al actualizar usuario: ${ex.message}")
            }
        }

        @DeleteMapping("/{id:.+}")
        fun eliminarUsuario(@PathVariable id: String): ResponseEntity<String> {
            return try {
                val rows = usuarioService.eliminarUsuario(id)
                if (rows > 0) ResponseEntity.ok("Usuario eliminado correctamente")
                else ResponseEntity.status(404).body("Usuario no encontrado con ese email o documento")
            } catch (ex: Exception) {
                ResponseEntity.status(500).body("Error al eliminar usuario: ${ex.message}")
            }
        }
    }