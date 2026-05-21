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

class ProductosScreen(
    private val viewModel: ProductosViewModel
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // LocalNavigator provee acceso al historial de pantallas actual
        val navigator = LocalNavigator.currentOrThrow
        val state by viewModel.state.collectAsState()
        var mostrarDialogoAgregar by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Inventario") },
                    navigationIcon = {
                        TextButton(onClick = { navigator.pop() }) { // Quita esta pantalla y vuelve a la anterior (ej. Login)
                            Text("< Atrás", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    actions = {
                        Button(
                            onClick = { viewModel.exportarAExcel() },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Exportar Excel")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { mostrarDialogoAgregar = true }) {
                    Text("+ Añadir", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                state.mensajeExportacion?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onIncrease = {
                                viewModel.actualizarStock(producto.id, producto.stock + 1)
                            },
                            onDecrease = {
                                viewModel.actualizarStock(producto.id, producto.stock - 1)
                            }
                        )
                    }
                }
            }
        }

        if (mostrarDialogoAgregar) {
            DialogoAgregarProducto(
                onDismiss = { mostrarDialogoAgregar = false },
                onConfirm = { nombre, precio, cantidad, categoria ->
                    viewModel.agregarNuevoProducto(nombre, precio, cantidad, categoria)
                    mostrarDialogoAgregar = false
                }
            )
        }
    }
}

@Composable
fun DialogoAgregarProducto(
    onDismiss: () -> Unit,
    onConfirm: (nombre: String, precio: Double, cantidad: Long, categoria: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") }
    var cantidadTexto by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Nuevo Producto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del producto") }, singleLine = true)
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría / Descripción") }, singleLine = true)
                OutlinedTextField(value = precioTexto, onValueChange = { precioTexto = it }, label = { Text("Precio") }, singleLine = true)
                OutlinedTextField(value = cantidadTexto, onValueChange = { cantidadTexto = it }, label = { Text("Cantidad inicial") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val precioSeguro = precioTexto.toDoubleOrNull() ?: 0.0
                    val cantidadSegura = cantidadTexto.toLongOrNull() ?: 0L
                    if (nombre.isNotBlank()) onConfirm(nombre, precioSeguro, cantidadSegura, categoria)
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ProductoItem(
    producto: Producto,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
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
                Text(text = "Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onDecrease, enabled = producto.stock > 0, contentPadding = PaddingValues(0.dp), modifier = Modifier.size(40.dp)) {
                    Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = "${producto.stock}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                OutlinedButton(onClick = onIncrease, contentPadding = PaddingValues(0.dp), modifier = Modifier.size(40.dp)) {
                    Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}