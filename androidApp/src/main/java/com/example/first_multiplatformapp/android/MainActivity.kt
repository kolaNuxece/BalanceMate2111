package com.example.first_multiplatformapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent // ВАЖЛИВО
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.first_multiplatformapp.XML.App
import com.example.first_multiplatformapp.Repository.CategoryRepository
import com.example.first_multiplatformapp.Repository.ExpenseRepository
import com.example.first_multiplatformapp.Database.createDatabase
import com.example.first_multiplatformapp.Database.DatabaseDriverFactory
import com.example.first_multiplatformapp.Repository.SettingsRepository
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel
import kotlinx.coroutines.MainScope
import com.russhwolf.settings.SharedPreferencesSettings

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driverFactory = DatabaseDriverFactory(applicationContext)
        val database = createDatabase(driverFactory)
        val repositoryEx = ExpenseRepository(database)
        val repositoryCt = CategoryRepository(database)
        val settings = SharedPreferencesSettings(
            applicationContext.getSharedPreferences("app_settings", MODE_PRIVATE)
        )
        val repositorySt = SettingsRepository(settings)
        val repositoryExpense = ExpenseRepository(database)


        val viewModelSt = SettingsViewModel(repositorySt, repositoryExpense, MainScope())
        val viewModel = ExpensViewModel(repositoryEx, repositoryCt)
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(viewModelSt, viewModel)
                }
            }
        }
    }

}
