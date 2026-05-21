package com.example.cafeteria

import kotlin.test.Test
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedLogicDesktopTest {

    @Test
    fun probarCreacionDeTablas() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

        try {
            database.Schema.create(driver)
            val database = database(driver)
            println("si se creo todo bien")
            assertTrue(true)

        } catch (e: Exception) {
            println("errorrrrrr en base de datos")
            e.printStackTrace()
            assertTrue(false, "la base de datos no pudo crearse")
        }
    }

    @Test
    fun probarInsertarYLeerDelDAO() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        database.Schema.create(driver)
        val database = database(driver)
        val repositorio = DAO(database)

        try {
            repositorio.agregarProducto(
                producto = "Ruffles rojos",
                precio = 25.5,
                cantidad = 50,
                descripcion = "Ruffles rojos picantes",
                proveedorId = 1,
                vendedorId = 1
            )

            val stockActual = database.databaseQueries.selectAllStock().executeAsList()

            assertTrue(stockActual.isNotEmpty(), "Ya agregue un producto, no deberia estar vacia")

            assertEquals("Café Americano", stockActual[0].producto)
            assertEquals(50, stockActual[0].cantidad)

            println("todo bien con el dao :)")

        } catch (e: Exception) {
            println("algo paso con el dao :(")
            e.printStackTrace()
            assertTrue(false, "el DAO falló")
        }
    }
}