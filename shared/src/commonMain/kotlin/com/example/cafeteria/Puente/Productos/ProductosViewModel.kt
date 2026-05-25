package com.example.cafeteria.Puente.Productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeteria.Domain.model.Producto
import com.example.cafeteria.Domain.Repository.ProductosRepository
import com.example.cafeteria.Domain.Repository.ExportadorRepository
import com.example.cafeteria.Domain.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductosRepository,
    private val exportador: ExportadorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductosUiState())
    val state = _state.asStateFlow()

    init { cargarProductos() }

    fun cargarProductos() {
        viewModelScope.launch {
            repository.obtenerTodosLosProductos().collect { res ->
                when (res) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                    is Resource.Success -> _state.update { it.copy(isLoading = false, productos = res.data) }
                    is Resource.Error -> _state.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }

    fun buscar(query: String) {
        viewModelScope.launch {
            repository.buscarProductoPorNombre(query).collect { res ->
                if (res is Resource.Success) _state.update { it.copy(productos = res.data) }
            }
        }
    }

    fun exportarAExcel() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val res = exportador.exportarStockAExcel(_state.value.productos)
            if (res is Resource.Success) _state.update { it.copy(isLoading = false, mensajeExportacion = "Excel guardado en: ${res.data}") }
        }
    }

    fun limpiarEstado() = _state.update { it.copy(error = null, operacionExitosa = false, mensajeExportacion = null) }

    fun actualizarStock(id: Long, nuevaCantidad: Long) {
        viewModelScope.launch {
            val res = repository.actualizarStock(id, nuevaCantidad)
            if (res is Resource.Success) {
                cargarProductos() // Recarga la lista para que la pantalla se actualice
            }
        }
    }

    fun agregarNuevoProducto(nombre: String, precio: Double, cantidad: Long, categoria: String) {
        viewModelScope.launch {
            val nuevoProducto = Producto(
                id = 0,
                nombre = nombre,
                precio = precio,
                stock = cantidad,
                categoria = categoria
            )

            // Pasamos 1L como ID temporal de proveedor y vendedor para cumplir
            // con las reglas de tu tabla Stock. Más adelante esto vendrá del Login.
            val res = repository.insertarProducto(nuevoProducto, proveedorId = 1L, vendedorId = 1L)

            if (res is Resource.Success) {
                cargarProductos() // Recargamos la lista para que el producto aparezca de inmediato
            }
        }
    }
}