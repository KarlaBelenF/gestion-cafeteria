package com.example.cafeteria

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.cafeteria.dih.AppModule

fun encenderBaseDeDatos() {
    try {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        database.Schema.create(driver)
        val db = database(driver)

        db.databaseQueries.insertVendedor("Admin", "admin", "1234")
        db.databaseQueries.insertProveedor("Proveedor Principal", "prov1", "pass")

        db.databaseQueries.insertProducto("Taco de Arrachera", 45.0, 20, "Corte estilo argentino", 1, 1)
        db.databaseQueries.insertProducto("Rebanada Pepperoni", 35.0, 8, "Con queso mozzarella", 1, 1)
        db.databaseQueries.insertProducto("Cheesy Bread", 40.0, 5, "Pan relleno de queso", 1, 1)
        db.databaseQueries.insertProducto("Empanada Cajeta", 25.0, 12, "Postre relleno", 1, 1)


        db.databaseQueries.insertVendedor("Belén", "belen", "123")
        db.databaseQueries.insertVendedor("Isaac", "isaac", "123")
        db.databaseQueries.insertVendedor("Funko", "funko", "123")
        db.databaseQueries.insertVendedor("Karla", "karla", "123")


        db.databaseQueries.insertCliente("Público General", "publico", "000")
        AppModule.database = db

    } catch (e: Exception) {
        println("Nota: No se pudo iniciar la BD temporal: ${e.message}")
    }
}