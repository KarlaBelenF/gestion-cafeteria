package com.example.cafeteria.Puente.auth

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false,
    val usuarioNombre: String = ""
)
