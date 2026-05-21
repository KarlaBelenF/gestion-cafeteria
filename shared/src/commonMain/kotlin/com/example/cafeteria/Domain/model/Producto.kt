package com.example.cafeteria.Domain.model

data class Producto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val stock: Long,
    val categoria: String
)