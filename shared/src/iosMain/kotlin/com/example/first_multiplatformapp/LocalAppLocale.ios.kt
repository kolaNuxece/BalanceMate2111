package com.example.first_multiplatformapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

// Переименовали переменную, чтобы не было конфликта с именем объекта
private val LocalIOSLocale = staticCompositionLocalOf {
    NSLocale.preferredLanguages.first() as String
}

actual object LocalAppLocale {
    private const val LANG_KEY = "AppleLanguages"
    private val default = NSLocale.preferredLanguages.first() as String

    actual val current: String
        @Composable get() = LocalIOSLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val new = value ?: default
        if (value == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey(LANG_KEY)
        } else {
            NSUserDefaults.standardUserDefaults.setObject(listOf(new), LANG_KEY)
        }
        return LocalIOSLocale.provides(new)
    }
}