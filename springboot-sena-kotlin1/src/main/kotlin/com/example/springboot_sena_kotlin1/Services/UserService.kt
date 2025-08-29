package com.example.springboot_sena_kotlin1.Services
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import com.example.springboot_sena_kotlin1.models.UserUpdateDTO
import com.example.springboot_sena_kotlin1.models.UsersName
import com.example.springboot_sena_kotlin1.models.Users

import com.example.springboot_sena_kotlin1.Repository.UsuarioRepository

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun listarUsuarios(): List<UsersName> {
        return usuarioRepository.allUsers()
    }

    fun detallesUsuarios(): List<Users> {
        return usuarioRepository.detallesUsuarios()
    }

    fun crearUsuario(usuario: Users) {
        val hashedPassword = passwordEncoder.encode(usuario.contrasena)
        usuarioRepository.crear(usuario, hashedPassword)
    }

    fun editarUsuario(dto: UserUpdateDTO, id: String): Int  {
        return usuarioRepository.editarUsuario(dto, id)
    }

    fun eliminarUsuario(id: String): Int {
        return usuarioRepository.eliminarUsuario(id)
    }

}
