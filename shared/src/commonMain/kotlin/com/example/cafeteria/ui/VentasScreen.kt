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
    private val ventasViewModel: VentasViewModel
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val productosState by productosViewModel.state.collectAsState()
        val ventasState by ventasViewModel.state.collectAsState()

        var productoAAgregar by remember { mutableStateOf<Producto?>(null) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Punto de Venta") },
                    navigationIcon = {
                        TextButton(onClick = { navigator.pop() }) {
                            Text("< Atrás", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Carrito: $${ventasState.totalCarrito}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Button(
                            onClick = {
                                // Aquí conectará la otra pantalla.
                                // Ejemplo: navigator.push(CobroScreen(ventasViewModel))
                                println("Botón Listo presionado. Esperando pantalla del companiero...")
                            },
                            enabled = ventasState.carrito.isNotEmpty()
                        ) {
                            Text("Listo ->", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                        ProductoVentaItem(
                            producto = producto,
                            onClickAgregar = { productoAAgregar = producto }
                        )
                    }
                }
            }
        }

        productoAAgregar?.let { producto ->
            DialogoAñadirAlCarrito(
                producto = producto,
                onDismiss = { productoAAgregar = null },
                onConfirm = { cantidadSeleccionada ->
                    ventasViewModel.agregarAlCarrito(
                        productoId = producto.id,
                        cantidad = cantidadSeleccionada,
                        precio = producto.precio
                    )
                    productoAAgregar = null
                }
            )
        }
    }
}

@Composable
fun ProductoVentaItem(
    producto: Producto,
    onClickAgregar: () -> Unit
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

            Button(onClick = onClickAgregar) {
                Text("Añadir")
            }
        }
    }
}

@Composable
fun DialogoAñadirAlCarrito(
    producto: Producto,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var cantidad by remember { mutableStateOf(1L) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir ${producto.nombre}") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("¿Cuántas unidades deseas agregar al carrito?")
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (cantidad > 1) cantidad-- },
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(text = "$cantidad", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    OutlinedButton(
                        onClick = { if (cantidad < producto.stock) cantidad++ },
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (cantidad == producto.stock) {
                    Text(
                        text = "Stock máximo alcanzado",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(cantidad) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}