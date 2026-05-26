package com.example.cafeteria

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {

    encenderBaseDeDatos()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Cafeteria"
    ) {
        App()
    }
}