package com.example.cafeteria

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.cafeteria.dih.AppModule

fun main() = application {

    encenderBaseDeDatos()

    AppModule.AppModule.inicializar(DatabaseDriverFactory())

    Window(
        onCloseRequest = ::exitApplication,
        title = "Cafeteria"
    ) {
        App()
    }
}