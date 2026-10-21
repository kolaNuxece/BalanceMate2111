package com.example.first_multiplatformapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform