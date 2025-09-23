package com.example.springboot_sena_kotlin1.Services

import com.example.springboot_sena_kotlin1.Repository.VehiculosAsignadosRepository
import com.example.springboot_sena_kotlin1.models.DTOS.VehiculosAsignados.VehiculosAsignadosDTO
import org.springframework.stereotype.Service
import java.sql.Timestamp
import com.example.springboot_sena_kotlin1.Services.ConductorService
import com.example.springboot_sena_kotlin1.Config.UtilJWT
import org.springframework.http.HttpStatus

@Service
class VehiculosAsignadosService(
    private val repository: VehiculosAsignadosRepository,
    private val conductorService: ConductorService,
    private val utilJWT: UtilJWT
) {

    fun getVehiculosAsignados(documentoOrEmail: String?): List<VehiculosAsignadosDTO> {
        val rows = repository.findRawVehiculosAsignados(documentoOrEmail)

        return rows.map { row ->
            val documento = row.getOrNull(0) as? String ?: ""
            val email = row.getOrNull(1) as? String ?: ""
            val placa = row.getOrNull(2) as? String ?: ""
            val marca = row.getOrNull(3) as? String ?: ""
            val tipo = row.getOrNull(4) as? String ?: ""
            val capacidad = (row.getOrNull(5) as? Number)?.toInt() ?: 0
            val estado = row.getOrNull(6) as? String ?: ""

            val ultimoMantenimiento = when (val v = row.getOrNull(7)) {
                is Timestamp -> v.toLocalDateTime()
                is java.util.Date -> Timestamp(v.time).toLocalDateTime()
                else -> null
            }

            val fechaAsignacionInicio = when (val v = row.getOrNull(8)) {
                is Timestamp -> v.toLocalDateTime()
                is java.util.Date -> Timestamp(v.time).toLocalDateTime()
                else -> null
            }

            var fechaEntregaVehiculo = when (val v = row.getOrNull(9)) {
                is Timestamp -> v.toLocalDateTime()
                is java.util.Date -> Timestamp(v.time).toLocalDateTime()
                else -> null
            }

            // Lógica de negocio mínima: si fechaEntrega es anterior a fechaAsignacion, normalizarla
            if (fechaAsignacionInicio != null && fechaEntregaVehiculo != null) {
                if (fechaEntregaVehiculo.isBefore(fechaAsignacionInicio)) {
                    fechaEntregaVehiculo = fechaAsignacionInicio
                }
            }

            VehiculosAsignadosDTO(
                documentoIdentidad = documento,
                email = email,
                placa = placa,
                marcaVehiculo = marca,
                tipoVehiculo = tipo,
                capacidad = capacidad,
                estadoVehiculo = estado,
                ultimoMantenimiento = ultimoMantenimiento,
                fechaAsignacionInicio = fechaAsignacionInicio,
                fechaEntregaVehiculo = fechaEntregaVehiculo
            )
        }
    }

    // Lógica de negocio relacionada con autenticación: validar token/extract subject y devolver datos o error
    fun getVehiculosAsignadosByToken(authHeader: String?): Pair<HttpStatus, Any> {
        val (status, body) = conductorService.obtenerPerfilAutenticado(authHeader)
        if (status != HttpStatus.OK) return status to body

        val subjectFromBody: String? = when (body) {
            is Map<*, *> -> {
                when {
                    body.containsKey("c_documentoIdentidad") -> (body["c_documentoIdentidad"] as? String)
                    body.containsKey("c_email") -> (body["c_email"] as? String)
                    body.containsKey("conductor") -> {
                        val conductor = body["conductor"]
                        if (conductor is Map<*, *>) {
                            (conductor["c_documentoIdentidad"] as? String) ?: (conductor["c_email"] as? String)
                        } else null
                    }
                    else -> null
                }
            }
            else -> null
        }

        val subject: String? = subjectFromBody ?: run {
            try {
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

                parsed?.let { tok ->
                    try {
                        utilJWT.extractUsername(tok)
                    } catch (ex: Exception) {
                        null
                    }
                }
            } catch (ex: Exception) {
                null
            }
        }

        if (subject.isNullOrBlank()) return HttpStatus.UNAUTHORIZED to mapOf("error" to "No se pudo determinar el usuario autenticado")

        return try {
            val lista = getVehiculosAsignados(subject)
            HttpStatus.OK to lista
        } catch (ex: Exception) {
            HttpStatus.INTERNAL_SERVER_ERROR to mapOf("error" to "Error al obtener vehículos asignados")
        }
    }
}
