package com.example.cafeteria

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform