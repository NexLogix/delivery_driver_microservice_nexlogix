package com.example.springboot_sena_kotlin1.Services

import com.example.springboot_sena_kotlin1.Repository.ConductorRepository
import com.example.springboot_sena_kotlin1.Config.UtilJWT
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

@Service
class ConductorService
(
    private val conductorRepository: ConductorRepository,
    private val utilJWT: UtilJWT
)
{
    private val passwordEncoder = BCryptPasswordEncoder()
    private val log = LoggerFactory.getLogger(ConductorService::class.java)

    // Nuevo: busca conductor por identificador (email o documento)
    fun obtenerConductorPorIdentificador(identifier: String): Map<String, Any?>? {
        return conductorRepository.infoConductorByIdentifier(identifier)
    }

    // Simplificado: 3 try-catch según lo solicitado
    fun obtenerPerfilAutenticado(authHeader: String?): Pair<HttpStatus, Any> {
        // TRY 1: parsear/normalizar header
        val token: String = try {
            var raw = authHeader?.trim()
            if (!raw.isNullOrBlank()) {
                if ((raw.startsWith("\"") && raw.endsWith("\"")) || (raw.startsWith("'") && raw.endsWith("'"))) {
                    raw = raw.substring(1, raw.length - 1)
                }
            }
            val parsed = raw?.let {
                val prefixLower = it.substring(0, minOf(it.length, 7)).lowercase()
                if (prefixLower == "bearer ") it.substring(7).trim() else it.trim()
            }?.takeIf { !it.isBlank() }

            if (parsed.isNullOrBlank()) {
                // no hay token -> error 401
                return HttpStatus.UNAUTHORIZED to mapOf("error" to "No autenticado: token requerido")
            }
            parsed
        } catch (ex: Exception) {
            log.warn("ConductorService - error parsing Authorization header", ex)
            return HttpStatus.UNAUTHORIZED to mapOf("error" to "Header Authorization inválido")
        }

        // TRY 2: validar token
        val valid: Boolean = try {
            utilJWT.validateToken(token)
        } catch (ex: Exception) {
            log.warn("ConductorService - excepción al validar token", ex)
            // si la validación lanza excepcion respondemos 200 con authenticated = false (según requerimiento)
            return HttpStatus.OK to mapOf("authenticated" to false)
        }
        if (!valid) {
            // token presente pero no autenticado -> 200 con estado de autenticación false
            log.warn("ConductorService - token inválido o expirado")
            return HttpStatus.OK to mapOf("authenticated" to false)
        }

        // TRY 3: extraer subject y consultar conductor
        val subject: String = try {
            utilJWT.extractUsername(token) ?: ""
        } catch (ex: Exception) {
            log.warn("ConductorService - error extrayendo subject del token", ex)
            return HttpStatus.UNAUTHORIZED to mapOf("error" to "Token inválido o error al procesarlo")
        }

        if (subject.isBlank()) {
            log.warn("ConductorService - subject vacío en token válido")
            return HttpStatus.UNAUTHORIZED to mapOf("error" to "Token no contiene subject")
        }

        return try {
            val info = obtenerConductorPorIdentificador(subject)
            if (info != null) {
                HttpStatus.OK to info
            } else {
                // token válido pero conductor no existe -> 200 con indicador
                HttpStatus.OK to mapOf("authenticated" to true, "conductor" to null)
            }
        } catch (ex: Exception) {
            log.error("ConductorService - error consultando conductor", ex)
            HttpStatus.INTERNAL_SERVER_ERROR to mapOf("error" to "Error al obtener conductor")
        }
    }

    // ...existing helper methods...
}
