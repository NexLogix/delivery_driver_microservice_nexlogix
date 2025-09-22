package com.example.springboot_sena_kotlin1.Repository

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityManager

@Repository
@Transactional(readOnly = true)
class RutasAsignadasRepository(private val entityManager: EntityManager) {

    fun findRawRutasAsignadas(documentoOrEmail: String?): List<Array<Any?>> {
        val sql = """
            SELECT
              c.c_documentoIdentidad,
              c.c_email,
              v.placa,
              v.marcaVehiculo,
              v.tipoVehiculo,
              v.capacidad,
              v.estadoVehiculo,
              avr.fechaAsignacionInicio,
              avr.fechaAsignacionFinalizacion,
              r.nombreRuta,
              r.horaInicioRuta,
              r.horaFinalizacionRuta,
              r.estadoRuta,
              r.descripcion
            FROM conductores c
            JOIN asignacion_conductor_por_vehiculos acpv ON acpv.idConductor = c.idConductor
            JOIN vehiculos v ON v.idVehiculo = acpv.idVehiculo
            JOIN Asignacion_Vehiculos_Por_Rutas avr ON avr.idVehiculo = v.idVehiculo
            JOIN rutas r ON r.idRuta = avr.idRuta
            WHERE (:p IS NULL OR c.c_documentoIdentidad = :p OR c.c_email = :p)
            ORDER BY avr.fechaAsignacionInicio DESC
        """.trimIndent()

        val query = entityManager.createNativeQuery(sql)
        query.setParameter("p", documentoOrEmail)

        @Suppress("UNCHECKED_CAST")
        return query.resultList as List<Array<Any?>>
    }
}

