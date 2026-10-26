package com.example.first_multiplatformapp.XML

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.first_multiplatformapp.customAppLocale
// Импортируйте ключи для текста, если вы их завели в strings.xml

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Profil / Настройки языка",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Кнопка переключения на русский
        Button(
            onClick = {
                // Меняем глобальную локаль на русский
                customAppLocale = "ru"
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("🇷🇺 Русский")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопка переключения на английский/дефолтный (или немецкий)
        Button(
            onClick = {
                // Меняем локаль на дефолтную (или "en")
                customAppLocale = "en"
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("🇬🇧 English")
        }
    }
}