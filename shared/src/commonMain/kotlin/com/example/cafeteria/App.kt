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
import androidx.compose.ui.unit.dp
import com.example.cafeteria.Puente.auth.screens_dani.LoginScreen
import com.example.cafeteria.dih.AppModule

@Composable
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
        }
    }
}