package com.example.cafeteria.Domain.Repository

import com.example.cafeteria.Domain.model.Producto
import com.example.cafeteria.Domain.util.Resource
interface ExportadorRepository {
    suspend fun exportarStockAExcel(productos: List<Producto>): Resource<String>
    suspend fun exportarStockAPDF(productos: List<Producto>): Resource<String>
}