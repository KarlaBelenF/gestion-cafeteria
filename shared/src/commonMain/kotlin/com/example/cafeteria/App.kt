package com.example.cafeteria

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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

@Composable
@Preview
fun App() {
    MaterialTheme {
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


//Si quieres que sea la pantalla inicial de la app

        // 1. Obtenemos el ViewModel desde las dependencias globales
        //val productosViewModel = remember { AppModule.AppModule.proveerProductosViewModel() }

        // 2. Inicializamos Voyager indicando cuál es la primera pantalla
        //Navigator(screen = ProductosScreen(viewModel = productosViewModel))
//Si la vas a llamar desde otra pantalla
// 1. Obtienes el navegador de Voyager que está controlando la app
//val navigator = LocalNavigator.currentOrThrow

// 2. Obtienes el ViewModel que necesita la pantalla de productos
//val productosViewModel = remember { AppModule.AppModule.proveerProductosViewModel() }

//Button(
//onClick = {
    // 3. Haces el "push" pasando la instancia de la pantalla con su ViewModel
    //navigator.push(ProductosScreen(productosViewModel))
//}
//) {
   // Text("Ingresar al Inventario")
//}