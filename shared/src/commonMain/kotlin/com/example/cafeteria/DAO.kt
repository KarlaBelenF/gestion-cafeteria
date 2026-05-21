package com.example.cafeteria
import com.example.cafeteria.database

class DAO (private val database: database) {
    private val queries = database.databaseQueries

    fun agregarProducto(producto: String, precio: Double, cantidad: Long, descripcion: String, proveedorId: Long, vendedorId: Long) {
        queries.insertProducto(producto, precio, cantidad, descripcion, proveedorId, vendedorId)
    }

    fun eliminarProducto(idProducto: Long) {
        queries.deleteProducto(idProducto)
    }

    fun registrarVenta(clienteId: Long, vendedorId: Long, stockId: Long, cantidadVendida: Long, precioUnitario: Double) {
        database.transaction {
            val totalVenta = cantidadVendida * precioUnitario

            queries.insertDetalleVenta(
                cliente_id = clienteId,
                vendedor_id = vendedorId,
                stock_id = stockId,
                cantidad = cantidadVendida,
                total = totalVenta
            )

            queries.updateStockQuantity(
                cantidad = cantidadVendida,
                id = stockId
            )
        }
    }
}