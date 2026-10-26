package com.example.first_multiplatformapp.XML


import androidx.compose.runtime.*

// Сюда будем передавать код выбранного языка (например, "ru" или "en")
var customAppLocale by mutableStateOf<String?>(null)

expect object LocalAppLocale {
    val current: String
        @Composable get
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

@Composable
fun AppEnvironment(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppLocale provides customAppLocale,
    ) {
        // key принудительно пересоздает дерево UI при смене языка
        key(customAppLocale) {
            content()
        }
    }
}