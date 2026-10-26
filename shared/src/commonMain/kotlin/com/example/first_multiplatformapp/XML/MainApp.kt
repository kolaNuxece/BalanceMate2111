package com.example.first_multiplatformapp.XML

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel
import first_multiplatformapp.shared.generated.resources.Res
import org.jetbrains.compose.resources.stringResource


@Composable
fun MainApp(
    viewModelEx: ExpensViewModel,
    viewModelSt: SettingsViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCharts by remember { mutableStateOf(false) }

    MaterialTheme {
        if (showCharts) {
            ChartsScreen(
                onBack = { showCharts = false },
                viewModelEx = viewModelEx
            )
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Главная"
                                )
                            },
                         //   label = { Text(stringResource(Res.string.greeting))}
                        )

                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Статистика"
                                )
                            },
                            label = { Text("Statistik") }
                        )

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Профіль"
                                )
                            },
                            label = { Text("Profil") }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedTab) {
                        0 -> MainScreen(
                            viewModel = viewModelEx,
                            viewModelSt = viewModelSt
                        )

                        1 -> DatabaseScreen(
                            viewModelEx = viewModelEx,
                            viewModelSt = viewModelSt,
                            onChartsClick = { showCharts = true }
                        )

                        2 -> ProfileScreen()
                    }
                }
            }
        }
    }
}
