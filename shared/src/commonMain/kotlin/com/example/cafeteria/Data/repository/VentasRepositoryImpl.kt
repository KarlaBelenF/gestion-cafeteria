package com.example.cafeteria.Data.repository

import com.example.cafeteria.Domain.Repository.VentasRepository
import com.example.cafeteria.Domain.model.Venta
import com.example.cafeteria.Domain.model.DetalleVenta
import com.example.cafeteria.Domain.util.Resource
import com.example.cafeteria.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VentasRepositoryImpl(
    private val database: database
) : VentasRepository {

    private val queries = database.databaseQueries

    override suspend fun registrarVenta(
        clienteId: Long,
        vendedorId: Long,
        detalles: List<DetalleVenta>
    ): Resource<Unit> {
        return try {
            database.transactionWithResult {
                for (detalle in detalles) {
                    val productoDb = queries.selectStockById(detalle.productoId).executeAsOneOrNull()

                    if (productoDb == null) {
                        rollback(Resource.Error("El producto con ID ${detalle.productoId} no existe en el Stock."))
                    }

                    if (productoDb.cantidad < detalle.cantidad) {
                        rollback(Resource.Error("Stock insuficiente para: ${productoDb.producto}"))
                    }

                    queries.updateStockQuantity(
                        cantidad = detalle.cantidad,
                        id = detalle.productoId
                    )

                    val totalCalculado = detalle.cantidad * detalle.precioUnitario

                    queries.insertDetalleVenta(
                        cliente_id = clienteId,
                        vendedor_id = vendedorId,
                        stock_id = detalle.productoId,
                        cantidad = detalle.cantidad,
                        total = totalCalculado
                    )
                }
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Resource.Error("Error en la transacción de venta: ${e.message}", e)
        }
    }

    override fun obtenerVentasPorFecha(fechaInicio: String, fechaFin: String): Flow<Resource<List<Venta>>> = flow {
        emit(Resource.Loading)
        try {

            val listado = queries.selectAllVentas().executeAsList().map {
                Venta(
                    id = it.id,
                    clienteId = it.cliente_id,
                    vendedorId = it.vendedor_id,
                    stockId = it.stock_id,
                    cantidad = it.cantidad,
                    total = it.total,
                    fecha = it.fecha
                )
            }
            emit(Resource.Success(listado))
        } catch (e: Exception) {
            emit(Resource.Error("Error al filtrar las ventas por rango", e))
        }
    }

    override fun obtenerReporteVentasTotales(): Flow<Resource<List<Venta>>> = flow {
        emit(Resource.Loading)
        try {
            val ventas = queries.selectAllVentas().executeAsList().map {
                Venta(
                    id = it.id,
                    clienteId = it.cliente_id,
                    vendedorId = it.vendedor_id,
                    stockId = it.stock_id,
                    cantidad = it.cantidad,
                    total = it.total,
                    fecha = "2026-05-19"
                )
            }
            emit(Resource.Success(ventas))
        } catch (e: Exception) {
            emit(Resource.Error("Error de lectura global", e))
        }
    }
}