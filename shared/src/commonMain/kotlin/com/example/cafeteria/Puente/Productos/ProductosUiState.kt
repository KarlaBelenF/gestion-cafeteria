package com.example.cafeteria.Puente.Productos

import com.example.cafeteria.Domain.model.Producto

data class ProductosUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val error: String? = null,
    val operacionExitosa: Boolean = false,
    val mensajeExportacion: String? = null
)