package com.example.springboot_sena_kotlin1

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringbootSenaKotlin1Application

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("SpringbootSenaKotlin1Application")
    // Verificar si application.properties está disponible en classpath
    val res = Thread.currentThread().contextClassLoader.getResource("application.properties")
    log.info("DIAGNOSTIC: application.properties on classpath = ${res?.toString() ?: "NOT FOUND"}")

    // Mostrar variables de entorno y propiedades del sistema relevantes
    val envUrl = System.getenv("SPRING_DATASOURCE_URL")
    val envUser = System.getenv("SPRING_DATASOURCE_USERNAME")
    val envPass = System.getenv("SPRING_DATASOURCE_PASSWORD")
    log.info("DIAGNOSTIC: env SPRING_DATASOURCE_URL = ${envUrl ?: "NULL"}")
    log.info("DIAGNOSTIC: env SPRING_DATASOURCE_USERNAME = ${envUser ?: "NULL"}")
    log.info("DIAGNOSTIC: env SPRING_DATASOURCE_PASSWORD = ${if (envPass != null) "SET" else "NULL"}")

    // Propiedad del sistema (si fue pasada con -D)
    val sysUrl = System.getProperty("spring.datasource.url")
    log.info("DIAGNOSTIC: system property spring.datasource.url = ${sysUrl ?: "NULL"}")

    // Si no se encontró application.properties, establecemos valores por defecto para que la app pueda arrancar
    if (res == null) {
        log.warn("application.properties not found on classpath — applying fallback datasource properties")
        System.setProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/logigov2?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC")
        System.setProperty("spring.datasource.username", "root")
        System.setProperty("spring.datasource.password", "rootpass123")
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver")
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update")
        System.setProperty("server.port", "8081")

        // Logging explícito de los valores aplicados para diagnóstico
        log.info("FALLBACK: spring.datasource.url = ${System.getProperty("spring.datasource.url")}")
        log.info("FALLBACK: spring.datasource.username = ${System.getProperty("spring.datasource.username")}")
        log.info("FALLBACK: spring.datasource.driver-class-name = ${System.getProperty("spring.datasource.driver-class-name")}")
    }

    runApplication<SpringbootSenaKotlin1Application>(*args)
}
