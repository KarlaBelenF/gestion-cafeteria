package com.example.cafeteria.Data.repository

import java.io.File

actual fun guardarArchivoFisico(nombre: String, contenido: String, contexto: Any?): String {
    val rutaUsuario = System.getProperty("user.home")
    val carpetaDescargas = File(rutaUsuario, "Downloads")

    val archivoDestino = if (carpetaDescargas.exists()) {
        File(carpetaDescargas, nombre)
    } else {
        File(rutaUsuario, nombre)
    }

    archivoDestino.writeText(contenido)
    return archivoDestino.absolutePath
}