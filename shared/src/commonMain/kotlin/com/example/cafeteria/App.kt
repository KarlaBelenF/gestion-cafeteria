package com.example.cafeteria

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import cafeteria.shared.generated.resources.Res
import cafeteria.shared.generated.resources.compose_multiplatform
import com.example.cafeteria.dih.AppModule
import com.example.cafeteria.ui.ProductosScreen
import com.example.cafeteria.ui.VentasScreen


//Si quieres que sea la pantalla inicial de la app
import androidx.compose.ui.unit.dp
import com.example.cafeteria.Puente.auth.screens_dani.LoginScreen
import com.example.cafeteria.dih.AppModule

@Composable
@Preview
fun App() {
    MaterialTheme {
        val loginViewModel = remember { AppModule.proveerLoginViewModel() }

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
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¡Bienvenido, $usuarioLogueado!",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text("Aquí irá tu pantalla de Visualización de Orden.")
                }
            }
        val productosViewModel = remember { AppModule.AppModule.proveerProductosViewModel() }
        val ventasViewModel = remember { AppModule.AppModule.proveerVentasViewModel() }

        // 1 = Pantalla de Productos (Inventario)
        // 2 = Pantalla de Ventas (Punto de Venta)
        val seleccion = 1

        // Determinamos qué objeto Screen instanciar según tu selección
        val pantallaInicial = when (seleccion) {
            1 -> ProductosScreen(viewModel = productosViewModel)
            2 -> VentasScreen(productosViewModel = productosViewModel, ventasViewModel = ventasViewModel)
            else -> ProductosScreen(viewModel = productosViewModel) // Respaldo por defecto
        }

        // Cargamos Voyager pasándole la pantalla elegida dinámicamente
        Navigator(screen = pantallaInicial) { navigator ->
            SlideTransition(navigator)
        }
    }
}