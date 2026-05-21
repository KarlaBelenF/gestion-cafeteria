package com.example.cafeteria.Domain.model

enum class Rol {
    CLIENTE,
    VENDEDOR,
    PROVEEDOR
}

data class Usuario(
    val id: Long,
    val nombre: String,
    val correo: String,
    val rol: Rol
)