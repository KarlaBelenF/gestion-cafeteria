package com.example.cafeteria.Domain.Repository
import com.example.cafeteria.Domain.model.Producto
import com.example.cafeteria.Domain.util.Resource
import kotlinx.coroutines.flow.Flow


interface ProductosRepository {
    fun obtenerTodosLosProductos(): Flow<Resource<List<Producto>>>
    fun buscarProductoPorNombre(query: String): Flow<Resource<List<Producto>>>
    suspend fun insertarProducto(producto: Producto, proveedorId: Long, vendedorId: Long): Resource<Unit>
    suspend fun eliminarProducto(id: Long): Resource<Unit>
}