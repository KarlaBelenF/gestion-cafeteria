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
import com.example.cafeteria.Puente.Productos.ProductosViewModel
import com.example.cafeteria.Puente.ventas.VentasViewModel

class CobroScreen(
    private val ventasViewModel: VentasViewModel,
    private val productosViewModel: ProductosViewModel
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val historial by ventasViewModel.historial.collectAsState()

        // El admin solo ve lo que falta por aceptar
        val pedidosPendientes = historial.filter { it.estado == "Pendiente" }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Órdenes Pendientes") },
                    navigationIcon = {
                        TextButton(onClick = { navigator.pop() }) {
                            Text("< Atrás", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                if (pedidosPendientes.isEmpty()) {
                    Text("¡No hay pedidos nuevos!", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(pedidosPendientes) { pedido ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Cliente: ${pedido.clienteNombre}", fontWeight = FontWeight.Bold)
                                        Text("Total: $${pedido.total}")
                                        Text("${pedido.items.size} tipos de artículos")
                                    }

                                    Button(onClick = {
                                        // 1. Cambiamos el estado a "Aceptado" para que al cliente le salga su QR
                                        ventasViewModel.aceptarPedidoMock(pedido.id)

                                        // 2. Le avisamos a la base de datos que descuente el inventario real
                                        pedido.items.forEach { articulo ->
                                            // 👇 AQUÍ: Tienes que usar la función que tu equipo haya programado
                                            // en ProductosViewModel para actualizar el stock.
                                            // Si la llamaron diferente, solo cámbiale el nombre.
                                            productosViewModel.reducirInventario(articulo.productoId, articulo.cantidad)
                                        }
                                    }) {
                                        Text("Aceptar Venta")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}