package com.example.cafeteria.Data.repository

import android.content.Context
import android.os.Environment
import java.io.File

actual fun guardarArchivoFisico(nombre: String, contenido: String, contexto: Any?): String {
    val ctx = contexto as? Context ?: throw IllegalArgumentException("Se requiere el Context de Android")

    val carpetaDescargas = ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val archivoDestino = File(carpetaDescargas, nombre)

    archivoDestino.writeText(contenido)
    return archivoDestino.absolutePath
}