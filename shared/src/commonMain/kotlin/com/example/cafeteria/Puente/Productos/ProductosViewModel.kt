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
}