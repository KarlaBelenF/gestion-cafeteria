package com.example.cafeteria.Puente.ventas

import com.example.cafeteria.Domain.model.Venta
import com.example.cafeteria.Domain.model.DetalleVenta

data class VentasUiState(
    val isLoading: Boolean = false,
    val carrito: List<DetalleVenta> = emptyList(),
    val ventaExitosa: Boolean = false,
    val error: String? = null,
    val ventasReporte: List<Venta> = emptyList(),
    val totalCarrito: Double = 0.0
)
