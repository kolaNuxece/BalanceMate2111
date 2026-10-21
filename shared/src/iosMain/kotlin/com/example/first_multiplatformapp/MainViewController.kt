package com.example.first_multiplatformapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.first_multiplatformapp.XML.App

fun MainViewController() = ComposeUIViewController {
    App() // Викликаємо ту саму функцію з commonMain
}
