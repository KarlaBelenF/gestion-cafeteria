package com.example.cafeteria
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:cafeteria.db")
        // Si la base de datos no existe en el disco de la compu, se crea el esquema automáticamente
        try {
            database.Schema.create(driver)
        } catch (e: Exception) {
            // El esquema ya estaba creado
        }
        return driver
    }
}