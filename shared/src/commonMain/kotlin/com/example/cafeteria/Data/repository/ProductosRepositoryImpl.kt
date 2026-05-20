package com.example.cafeteria.Data.repository

import com.example.cafeteria.Domain.Repository.ProductosRepository
import com.example.cafeteria.Domain.model.Producto
import com.example.cafeteria.Domain.util.Resource
import com.example.cafeteria.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductosRepositoryImpl(
    private val database: database
) : ProductosRepository {

    private val queries = database.databaseQueries

    override fun obtenerTodosLosProductos(): Flow<Resource<List<Producto>>> = flow {
        emit(Resource.Loading)
        try {
            val stockDb = queries.selectAllStock().executeAsList()
            val productosDominio = stockDb.map {
                Producto(
                    id = it.id,
                    nombre = it.producto,
                    precio = it.precio,
                    stock = it.cantidad,
                    categoria = it.descripcion ?: "Sin descripción"
                )
            }
            emit(Resource.Success(productosDominio))
        } catch (e: Exception) {
            emit(Resource.Error("Error al cargar el stock", e))
        }
    }

    override fun buscarProductoPorNombre(query: String): Flow<Resource<List<Producto>>> = flow {
        emit(Resource.Loading)
        try {
            val filtrados = queries.selectAllStock().executeAsList()
                .filter { it.producto.contains(query, ignoreCase = true) }
                .map { Producto(it.id, it.producto, it.precio, it.cantidad, it.descripcion ?: "") }
            emit(Resource.Success(filtrados))
        } catch (e: Exception) {
            emit(Resource.Error("Error en la búsqueda", e))
        }
    }

    override suspend fun insertarProducto(producto: Producto, proveedorId: Long, vendedorId: Long): Resource<Unit> {
        return try {
            queries.insertProducto(
                producto = producto.nombre,
                precio = producto.precio,
                cantidad = producto.stock,
                descripcion = producto.categoria,
                proveedor_id = proveedorId,
                vendedor_id = vendedorId
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al insertar el nuevo producto", e)
        }
    }

    override suspend fun eliminarProducto(id: Long): Resource<Unit> {
        return try {
            queries.deleteProducto(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al eliminar del inventario", e)
        }
    }
}