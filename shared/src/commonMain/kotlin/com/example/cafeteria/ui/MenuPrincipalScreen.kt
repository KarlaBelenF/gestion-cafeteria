package com.example.cafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.cafeteria.Puente.Productos.ProductosViewModel
import com.example.cafeteria.Puente.ventas.VentasViewModel

class MenuPrincipalScreen(
    private val productosViewModel: ProductosViewModel,
    private val ventasViewModel: VentasViewModel,
    private val esAdmin: Boolean
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido al Sistema", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = if (esAdmin) "Modo: Administrador" else "Modo: Cliente",
                color = if (esAdmin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (esAdmin) {
                Button(onClick = {
                    navigator.push(ProductosScreen(productosViewModel))
                }) {
                    Text("Ir a Inventario (Productos)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navigator.push(CobroScreen(ventasViewModel, productosViewModel))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Administrar Órdenes Pendientes")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}