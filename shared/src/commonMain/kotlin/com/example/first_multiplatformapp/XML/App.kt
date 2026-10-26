package com.example.first_multiplatformapp.XML

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.first_multiplatformapp.AppEnvironment
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel

@Composable
fun App(
    viewModelSt: SettingsViewModel,
    viewModel: ExpensViewModel
) {
    AppEnvironment{
    val isFirstLaunch by viewModelSt.isFirstLaunch.collectAsState()

    if (isFirstLaunch) {
        SalaryScreen(viewModelSt)
    } else {
        MainApp(viewModel, viewModelSt)
    }
    }
}
