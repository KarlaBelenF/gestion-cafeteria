package com.example.cafeteria.Domain.model

data class Venta(
    val id: Long,
    val clienteId: Long,
    val vendedorId: Long,
    val stockId: Long,
    val cantidad: Long,
    val total: Double,
    val fecha: String,
    val nombreProducto: String = ""
)
data class DetalleVenta(
    val productoId: Long,
    val cantidad: Long,
    val precioUnitario: Double
)