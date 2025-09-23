package com.example.springboot_sena_kotlin1.Repository

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityManager

@Repository
@Transactional(readOnly = true)
class VehiculosAsignadosRepository(private val entityManager: EntityManager) {

    fun findRawVehiculosAsignados(documentoOrEmail: String?): List<Array<Any?>> {
        val sql = """
            SELECT
              c.c_documentoIdentidad,
              c.c_email,
              v.placa,
              v.marcaVehiculo,
              v.tipoVehiculo,
              v.capacidad,
              v.estadoVehiculo,
              v.ultimoMantenimiento,
              acpv.fecha_asignacion_vehiculo AS fechaAsignacionInicio,
              acpv.fecha_entrega_vehiculo   AS fechaEntregaVehiculo
            FROM conductores c
            JOIN asignacion_conductor_por_vehiculos acpv ON acpv.idConductor = c.idConductor
            JOIN vehiculos v ON v.idVehiculo = acpv.idVehiculo
            WHERE (:p IS NULL OR c.c_documentoIdentidad = :p OR c.c_email = :p)
            ORDER BY acpv.fecha_asignacion_vehiculo DESC
        """.trimIndent()

        val query = entityManager.createNativeQuery(sql)
        query.setParameter("p", documentoOrEmail)

        @Suppress("UNCHECKED_CAST")
        return query.resultList as List<Array<Any?>>
    }
}
