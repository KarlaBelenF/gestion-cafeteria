package com.example.cafeteria

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.cafeteria.dih.AppModule
import com.example.cafeteria.ui.ProductosScreen
import com.example.cafeteria.ui.VentasScreen
import com.example.cafeteria.Puente.auth.screens_dani.LoginScreen
import com.example.cafeteria.ui.MenuPrincipalScreen
import com.example.cafeteria.Puente.Productos.ProductosViewModel
import com.example.cafeteria.Puente.ventas.VentasViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val loginViewModel = remember { AppModule.proveerLoginViewModel() }
        val productosViewModel = remember { AppModule.proveerProductosViewModel() }
        val ventasViewModel = remember { AppModule.proveerVentasViewModel() }
        var usuarioLogueado by remember { mutableStateOf<String?>(null) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (usuarioLogueado == null) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginExitoso = { nombre ->
                        usuarioLogueado = nombre
                    }
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            usuarioLogueado = null
                        }) {
                            Text("Cerrar Sesión de $usuarioLogueado", color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        val esAdmin = usuarioLogueado.equals("Admin", ignoreCase = true)

                        if (esAdmin) {
                            Navigator(
                                screen = MenuPrincipalScreen(
                                    productosViewModel = productosViewModel,
                                    ventasViewModel = ventasViewModel,
                                    esAdmin = true
                                )
                            ) { navigator ->
                                SlideTransition(navigator)
                            }
                        } else {
                            Navigator(
                                screen = VentasScreen(
                                    productosViewModel = productosViewModel,
                                    ventasViewModel = ventasViewModel,
                                    clienteNombre = usuarioLogueado ?: "Cliente"
                                )
                            ) { navigator ->
                                SlideTransition(navigator)
                            }
                        }
                    }
                }
            }
        }
    }
}