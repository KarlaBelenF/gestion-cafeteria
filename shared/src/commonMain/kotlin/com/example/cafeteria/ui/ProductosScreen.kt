package com.example.cafeteria.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
        val navigator = LocalNavigator.currentOrThrow
        val state by viewModel.state.collectAsState()

        // Estados para controlar los diálogos emergentes
        var mostrarDialogoAgregar by remember { mutableStateOf(false) }
        var mostrarGrafica by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Inventario") },
                    navigationIcon = {
                        TextButton(onClick = { navigator.pop() }) {
                            Text("< Atrás", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    actions = {
                        // NUEVO BOTÓN PARA MOSTRAR LA GRÁFICA
                        Button(
                            onClick = { mostrarGrafica = true },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Ver Gráfica")
                        }

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

        // Diálogo para agregar producto
        if (mostrarDialogoAgregar) {
            DialogoAgregarProducto(
                onDismiss = { mostrarDialogoAgregar = false },
                onConfirm = { nombre, precio, cantidad, categoria ->
                    viewModel.agregarNuevoProducto(nombre, precio, cantidad, categoria)
                    mostrarDialogoAgregar = false
                }
            )
        }

        // DIÁLOGO EMERGENTE QUE CONTIENE LA GRÁFICA DE BARRAS
        if (mostrarGrafica) {
            DialogoGraficaProductos(
                productos = state.productos,
                onDismiss = { mostrarGrafica = false }
            )
        }
    }
}

// COMPOSABLE DE LA GRÁFICA DE BARRAS NATIVA
@Composable
fun DialogoGraficaProductos(
    productos: List<Producto>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Stock por Producto", fontWeight = FontWeight.Bold) },
        text = {
            if (productos.isEmpty()) {
                Text("No hay productos registrados en el inventario para graficar.")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // Buscamos el stock máximo para que sirva como el 100% de la altura de la gráfica
                    val maxStock = productos.maxOfOrNull { it.stock }?.coerceAtLeast(1L) ?: 1L
                    val scrollState = rememberScrollState()

                    // Contenedor horizontal con Scroll para que entren infinitos productos sin romperse
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .horizontalScroll(scrollState)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        productos.forEach { producto ->
                            // Calculamos la altura de la barra de forma proporcional (máximo 180.dp)
                            val porcentajeAltura = producto.stock.toFloat() / maxStock.toFloat()
                            val alturaBarra = (porcentajeAltura * 180).dp.coerceAtLeast(4.dp)

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(65.dp)
                            ) {
                                // Texto indicador de la cantidad encima de la barra
                                Text(
                                    text = "${producto.stock}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // El cuerpo físico de la barra
                                Box(
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(alturaBarra)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                        )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Etiqueta inferior con el nombre del producto
                                Text(
                                    text = producto.nombre,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
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