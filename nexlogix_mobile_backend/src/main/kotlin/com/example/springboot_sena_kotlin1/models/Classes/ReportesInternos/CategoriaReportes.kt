package com.example.springboot_sena_kotlin1.models.Classes.ReportesInternos

data class CategoriaReportes(
    val idcategoria: Long? = null, // puede ser null porque es autoincrement
    val nombreCategoria: String
)