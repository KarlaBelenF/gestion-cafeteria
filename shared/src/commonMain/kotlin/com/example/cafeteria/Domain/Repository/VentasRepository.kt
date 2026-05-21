package com.example.cafeteria.Domain.Repository

import com.example.cafeteria.Domain.model.DetalleVenta
import com.example.cafeteria.Domain.model.Venta
import com.example.cafeteria.Domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface VentasRepository {
    suspend fun registrarVenta(clienteId: Long, vendedorId: Long, detalles: List<DetalleVenta>): Resource<Unit>
    fun obtenerVentasPorFecha(fechaInicio: String, fechaFin: String): Flow<Resource<List<Venta>>>
    fun obtenerReporteVentasTotales(): Flow<Resource<List<Venta>>>
}