package com.example.cafeteria.Data.repository

import com.example.cafeteria.Domain.util.Resource
import com.example.cafeteria.database
import com.example.cafeteria.Domain.model.Usuario
import com.example.cafeteria.Domain.model.Rol
class AuthRepositoryImpl(private val database: database) {

    private val queries = database.databaseQueries

    suspend fun loginVendedor(usuario: String, contrasena: String): Resource<Usuario> {
        return try {
            val vendedorDb = queries.selectVendedorLogin(usuario, contrasena).executeAsOneOrNull()

            if (vendedorDb != null) {
                Resource.Success(
                    Usuario(
                        id = vendedorDb.id,
                        nombre = vendedorDb.nombre,
                        correo = vendedorDb.usuario,
                        rol = Rol.VENDEDOR
                    )
                )
            } else {
                Resource.Error("Usuario o contraseña incorrectos")
            }
        } catch (e: Exception) {
            Resource.Error("Error al conectar con la base de datos")
        }
    }
}