package com.example.springboot_sena_kotlin1.Repository

import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.crypto.password.PasswordEncoder
import com.example.springboot_sena_kotlin1.Config.UtilJWT

@Repository
class AuthLoginRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val passwordEncoder: PasswordEncoder,
    private val utilJWT: UtilJWT
) {
    fun loginByEmail(email: String, rawPassword: String): String? {
        val sql = "SELECT contrasena FROM conductores WHERE c_email = ?"
        return try {
            val encodedPassword = jdbcTemplate.queryForObject(sql, String::class.java, email) ?: return null
            if (passwordEncoder.matches(rawPassword, encodedPassword)) {
                utilJWT.generateToken(email)
            } else {
                null
            }
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    fun login(identifier: String, rawPassword: String): String? {
        // si identifier contiene '@' se asume email, si no se intenta por c_documentoIdentidad
        val (sql, param) = if (identifier.contains("@")) {
            "SELECT contrasena FROM conductores WHERE c_email = ?" to identifier
        } else {
            "SELECT contrasena FROM conductores WHERE c_documentoIdentidad = ?" to identifier
        }
        return try {
            val encodedPassword = jdbcTemplate.queryForObject(sql, String::class.java, param) ?: return null
            if (passwordEncoder.matches(rawPassword, encodedPassword)) {
                // generar token usando el email si lo tienes; aquí usamos el identificador para el subject
                utilJWT.generateToken(identifier)
            } else {
                null
            }
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }
}