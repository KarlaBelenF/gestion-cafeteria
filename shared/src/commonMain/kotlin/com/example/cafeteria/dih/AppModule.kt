package com.example.cafeteria.dih

import com.example.cafeteria.Data.repository.AuthRepositoryImpl // Agregamos tu repositorio
import com.example.cafeteria.Data.repository.ExportadorRepositoryImpl
import com.example.cafeteria.database
import com.example.cafeteria.Data.repository.ProductosRepositoryImpl
import com.example.cafeteria.Data.repository.VentasRepositoryImpl
import com.example.cafeteria.Domain.Repository.ExportadorRepository
import com.example.cafeteria.Domain.Repository.ProductosRepository
import com.example.cafeteria.Domain.Repository.VentasRepository
import com.example.cafeteria.Puente.auth.LoginViewModel // Agregamos tu ViewModel
import com.example.cafeteria.Puente.Productos.ProductosViewModel
import com.example.cafeteria.Puente.ventas.VentasViewModel

object AppModule {

    lateinit var database: database

    val ventasRepository: VentasRepository by lazy {
        VentasRepositoryImpl(database)
    }

    val productosRepository: ProductosRepository by lazy {
        ProductosRepositoryImpl(database)
    }

    val exportadorRepository: ExportadorRepository by lazy {
        ExportadorRepositoryImpl()
    }

    val authRepository: AuthRepositoryImpl by lazy {
        AuthRepositoryImpl(database)
    }

    fun proveerVentasViewModel(): VentasViewModel {
        return VentasViewModel(ventasRepository)
    }

    fun proveerProductosViewModel(): ProductosViewModel {
        return ProductosViewModel(productosRepository, exportadorRepository)
    }

    fun proveerLoginViewModel(): LoginViewModel {
        return LoginViewModel(authRepository)
    }
}