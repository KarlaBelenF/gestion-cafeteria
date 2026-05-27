package com.example.cafeteria.Puente.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeteria.Domain.model.DetalleVenta
import com.example.cafeteria.Domain.Repository.VentasRepository
import com.example.cafeteria.Domain.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PedidoEnMemoria(
    val id: Long = System.currentTimeMillis(),
    val clienteNombre: String,
    val items: List<DetalleVenta>,
    val total: Double,
    val estado: String = "Pendiente"
)

class VentasViewModel(private val repository: VentasRepository) : ViewModel() {

    private val _state = MutableStateFlow(VentasUiState())
    val state = _state.asStateFlow()

    private val _historial = MutableStateFlow<List<PedidoEnMemoria>>(emptyList())
    val historial = _historial.asStateFlow()

    fun agregarAlCarrito(productoId: Long, cantidad: Long, precio: Double) {
        _state.update { estadoActual ->
            val carritoActual = estadoActual.carrito.toMutableList()
            val index = carritoActual.indexOfFirst { it.productoId == productoId }

            if (index != -1) {
                val itemExistente = carritoActual[index]
                val nuevaCantidad = itemExistente.cantidad + cantidad
                if (nuevaCantidad > 0) carritoActual[index] = itemExistente.copy(cantidad = nuevaCantidad)
                else carritoActual.removeAt(index)
            } else {
                if (cantidad > 0) carritoActual.add(DetalleVenta(productoId, cantidad, precio))
            }

            estadoActual.copy(
                carrito = carritoActual,
                totalCarrito = carritoActual.sumOf { it.cantidad * it.precioUnitario }
            )
        }
    }

    fun enviarPedidoPendiente(clienteNombre: String) {
        val carritoActual = _state.value.carrito
        if (carritoActual.isEmpty()) return

        val nuevoPedido = PedidoEnMemoria(
            clienteNombre = clienteNombre,
            items = carritoActual,
            total = _state.value.totalCarrito
        )

        _historial.update { it + nuevoPedido }
        _state.update { it.copy(carrito = emptyList(), totalCarrito = 0.0) }
    }

    fun aceptarPedidoMock(pedidoId: Long) {
        _historial.update { lista ->
            lista.map { if (it.id == pedidoId) it.copy(estado = "Aceptado") else it }
        }
    }

    fun generarQrTicket(ventaId: Long, totalPedido: Double): String {
        return """
        CAFETERÍA ITSUR 
        -----------------------------
        Orden: #$ventaId
        Total: $$totalPedido
        Estado: PAGADO
        -----------------------------
        ¡Gracias por tu preferencia!
    """.trimIndent()
    }

    fun limpiarEstado() = _state.update { it.copy(error = null, ventaExitosa = false) }
}
