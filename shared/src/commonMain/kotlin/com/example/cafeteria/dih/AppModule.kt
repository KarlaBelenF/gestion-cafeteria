package com.example.cafeteria.dih

import com.example.cafeteria.DatabaseDriverFactory
import com.example.cafeteria.Data.repository.ExportadorRepositoryImpl
import com.example.cafeteria.database
import com.example.cafeteria.Data.repository.ProductosRepositoryImpl
import com.example.cafeteria.Data.repository.VentasRepositoryImpl
import com.example.cafeteria.Domain.Repository.ExportadorRepository
import com.example.cafeteria.Domain.Repository.ProductosRepository
import com.example.cafeteria.Domain.Repository.VentasRepository
import com.example.cafeteria.Puente.Productos.ProductosViewModel
import com.example.cafeteria.Puente.ventas.VentasViewModel

object AppModule {

    object AppModule {

        lateinit var database: database

        fun inicializar(factory: DatabaseDriverFactory) {
            if (!this::database.isInitialized) {
                database = database(factory.createDriver())
            }
        }

        // ¡Solo una vez!
        val ventasRepository: VentasRepository by lazy {
            VentasRepositoryImpl(database)
        }

        val productosRepository: ProductosRepository by lazy {
            ProductosRepositoryImpl(database)
        }

        val exportadorRepository: ExportadorRepository by lazy {
            ExportadorRepositoryImpl()
        }

        fun proveerVentasViewModel(): VentasViewModel {
            return VentasViewModel(ventasRepository)
        }

        fun proveerProductosViewModel(): ProductosViewModel {
            return ProductosViewModel(productosRepository, exportadorRepository)
        }
    }
}