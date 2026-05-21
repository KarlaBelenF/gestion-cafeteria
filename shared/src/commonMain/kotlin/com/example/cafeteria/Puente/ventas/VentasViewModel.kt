package com.example.cafeteria.Puente.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeteria.Domain.model.DetalleVenta
import com.example.cafeteria.Domain.Repository.VentasRepository
import com.example.cafeteria.Domain.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VentasViewModel(private val repository: VentasRepository) : ViewModel() {

    private val _state = MutableStateFlow(VentasUiState())
    val state = _state.asStateFlow()

    fun agregarAlCarrito(productoId: Long, cantidad: Long, precio: Double) {
        val nuevoDetalle = DetalleVenta(productoId, cantidad, precio)
        _state.update {
            val nuevoCarrito = it.carrito + nuevoDetalle
            it.copy(
                carrito = nuevoCarrito,
                totalCarrito = nuevoCarrito.sumOf { d -> d.cantidad * d.precioUnitario }
            )
        }
    }

    fun cobrar(clienteId: Long, vendedorId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val res = repository.registrarVenta(clienteId, vendedorId, _state.value.carrito)
            when (res) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, ventaExitosa = true, carrito = emptyList(), totalCarrito = 0.0) }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = res.message) }
                else -> Unit
            }
        }
    }

    fun cargarReporte(inicio: String, fin: String) {
        viewModelScope.launch {
            repository.obtenerVentasPorFecha(inicio, fin).collect { res ->
                if (res is Resource.Success) _state.update { it.copy(ventasReporte = res.data) }
            }
        }
    }

    fun generarQrTicket(ventaId: Long): String {
        return "ID_VENTA:$ventaId|TOTAL:${_state.value.totalCarrito}|FECHA_SISTEMA"
    }

    fun limpiarEstado() = _state.update { it.copy(error = null, ventaExitosa = false) }
}