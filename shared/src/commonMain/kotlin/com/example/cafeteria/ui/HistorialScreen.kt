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
import com.example.cafeteria.Puente.ventas.VentasViewModel

class HistorialScreen(
    private val ventasViewModel: VentasViewModel,
    private val clienteNombre: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val historial by ventasViewModel.historial.collectAsState()
        val misPedidos = historial.filter { it.clienteNombre == clienteNombre }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mis Pedidos") },
                    navigationIcon = {
                        TextButton(onClick = { navigator.pop() }) {
                            Text("< Volver a Tienda", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                if (misPedidos.isEmpty()) {
                    Text("Aún no tienes pedidos en el historial.", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(misPedidos.reversed()) { pedido ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Orden #${pedido.id}", fontWeight = FontWeight.Bold)
                                    Text("Total: $${pedido.total}", color = MaterialTheme.colorScheme.primary)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (pedido.estado == "Pendiente") {
                                        Text("Estado: Preparando... Espera a que caja lo acepte.", color = MaterialTheme.colorScheme.secondary)
                                    } else {
                                        Text("Estado: ¡Aceptado!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Surface(
                                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text("TICKET DE COMPRA", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)

                                                Spacer(modifier = Modifier.height(8.dp))

                                                val contenidoQr = ventasViewModel.generarQrTicket(pedido.id, pedido.total)

                                                CodigoQrReal(
                                                    contenido = contenidoQr,
                                                    modifier = Modifier
                                                        .padding(8.dp)
                                                        .size(150.dp)
                                                )


                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("¡Gracias por tu preferencia!", style = MaterialTheme.typography.labelSmall)
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
    }
}