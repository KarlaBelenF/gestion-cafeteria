package com.example.cafeteria.Puente.auth.screens_dani

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cafeteria.Puente.auth.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var usuarioTexto by remember { mutableStateOf("") }
    var contrasenaTexto by remember { mutableStateOf("") }

    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) {
            onLoginExitoso(state.usuarioNombre)
            viewModel.limpiarEstado() // Limpiamos para que no se quede guardado si cierran sesión
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sistema de Cafetería",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = usuarioTexto,
            onValueChange = { usuarioTexto = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasenaTexto,
            onValueChange = { contrasenaTexto = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Oculta el texto con puntitos
            singleLine = true
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.intentarLogin(usuarioTexto, contrasenaTexto) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Entrar")
            }
        }
    }
}