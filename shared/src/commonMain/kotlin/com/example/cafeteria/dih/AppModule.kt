package com.example.cafeteria.dih


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
        val ventasRepository: VentasRepository by lazy {
            VentasRepositoryImpl(database)
        }

        val productosRepository: ProductosRepository by lazy {
            ProductosRepositoryImpl(database)
        }
        fun proveerVentasViewModel(): VentasViewModel {
            return VentasViewModel(ventasRepository)
        }

        fun proveerProductosViewModel(): ProductosViewModel {
            return ProductosViewModel(productosRepository, exportadorRepository)
        }

        val exportadorRepository: ExportadorRepository by lazy {
            ExportadorRepositoryImpl()
        }
    }

}