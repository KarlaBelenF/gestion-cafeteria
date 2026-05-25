package com.example.cafeteria

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.cafeteria.dih.AppModule

fun encenderBaseDeDatos() {
    try {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        database.Schema.create(driver)
        AppModule.database = database(driver)
    } catch (e: Exception) {
        println("Nota: No se pudo iniciar la BD temporal.")
    }
}