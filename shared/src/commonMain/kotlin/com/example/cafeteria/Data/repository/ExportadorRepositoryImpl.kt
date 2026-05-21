package com.example.cafeteria.Data.repository


import com.example.cafeteria.Domain.Repository.ExportadorRepository
import com.example.cafeteria.Domain.model.Producto
import com.example.cafeteria.Domain.util.Resource

class ExportadorRepositoryImpl : ExportadorRepository {

    override suspend fun exportarStockAExcel(productos: List<Producto>): Resource<String> {
        return try {
            val constructorDeExcel = StringBuilder()
            constructorDeExcel.append("ID,Nombre_Producto,Precio,Stock_Disponible,Categoria\n")

            for (producto in productos) {
                constructorDeExcel.append("${producto.id},${producto.nombre},${producto.precio},${producto.stock},${producto.categoria}\n")
            }


            val rutaAbsoluta = guardarArchivoFisico(
                nombre = "Reporte_Inventario_Productos.csv",
                contenido = constructorDeExcel.toString(),
                contexto = null
            )

            Resource.Success(rutaAbsoluta)
        } catch (e: Exception) {
            Resource.Error("Error al generar el archivo Excel: ${e.message}")
        }
    }

    override suspend fun exportarStockAPDF(productos: List<Producto>): Resource<String> {
        return try {
            val constructorDePdf = StringBuilder()
            constructorDePdf.append("REPORTE DE INVENTARIO - CAFETERÍA\n")
            constructorDePdf.append("-----------------------------------\n")

            for (producto in productos) {
                constructorDePdf.append("[ID: ${producto.id}] ${producto.nombre} | Stock: ${producto.stock} | $${producto.precio}\n")
            }

            val rutaAbsoluta = guardarArchivoFisico(
                nombre = "Reporte_Inventario_Productos.pdf",
                contenido = constructorDePdf.toString(),
                contexto = null
            )

            Resource.Success(rutaAbsoluta)
        } catch (e: Exception) {
            Resource.Error("Error al generar el archivo PDF: ${e.message}")
        }
    }
}