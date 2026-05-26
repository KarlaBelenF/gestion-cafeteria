package com.example.cafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.cafeteria.Domain.model.Producto
import com.example.cafeteria.Puente.Productos.ProductosViewModel
import com.example.cafeteria.Puente.ventas.VentasViewModel


class VentasScreen(
    private val productosViewModel: ProductosViewModel,
    private val ventasViewModel: VentasViewModel,
    private val clienteNombre: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val productosState by productosViewModel.state.collectAsState()
        val ventasState by ventasViewModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tienda - Hola $clienteNombre") },
                    actions = {
                        Button(onClick = { navigator.push(HistorialScreen(ventasViewModel, clienteNombre)) }) {
                            Text("Ver Mis Pedidos")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Carrito: $${ventasState.totalCarrito}",
                            fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                        )

                        Button(
                            onClick = {
                                ventasViewModel.enviarPedidoPendiente(clienteNombre)
                                navigator.push(HistorialScreen(ventasViewModel, clienteNombre))
                            },
                            enabled = ventasState.carrito.isNotEmpty()
                        ) {
                            Text("Hacer Pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Selecciona los productos para la venta:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (productosState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val productosDisponibles = productosState.productos.filter { it.stock > 0 }

                    items(productosDisponibles) { producto ->
                        val itemEnCarrito = ventasState.carrito.find { it.productoId == producto.id }
                        val cantidadEnCarrito = itemEnCarrito?.cantidad ?: 0L

                        ProductoVentaItem(
                            producto = producto,
                            cantidadEnCarrito = cantidadEnCarrito,
                            onIncrementar = {
                                ventasViewModel.agregarAlCarrito(producto.id, 1L, producto.precio)
                            },
                            onDecrementar = {
                                ventasViewModel.agregarAlCarrito(producto.id, -1L, producto.precio)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoVentaItem(
    producto: Producto,
    cantidadEnCarrito: Long,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Stock disponible: ${producto.stock}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDecrementar,
                    enabled = cantidadEnCarrito > 0,
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$cantidadEnCarrito",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = onIncrementar,
                    enabled = cantidadEnCarrito < producto.stock,
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}