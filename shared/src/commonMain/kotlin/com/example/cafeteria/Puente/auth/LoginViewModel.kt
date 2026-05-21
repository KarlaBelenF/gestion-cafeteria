package com.example.cafeteria.Puente.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeteria.Data.repository.AuthRepositoryImpl
import com.example.cafeteria.Domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepositoryImpl) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun intentarLogin(usuario: String, contrasena: String) {
        if (usuario.isBlank() || contrasena.isBlank()) {
            _state.update { it.copy(error = "Llena todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val res = repository.loginVendedor(usuario, contrasena)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, loginExitoso = true, usuarioNombre = res.data.nombre) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = res.message) }
                }
                else -> Unit
            }
        }
    }

    fun limpiarEstado() = _state.update { LoginUiState() }
}