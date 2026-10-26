package com.example.first_multiplatformapp.XML

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel

@Composable
fun SalaryScreen(viewModelSt: SettingsViewModel){


    var salaryInput by remember { mutableStateOf("") }

    Column {
        Text("Gehalt eingeben")

        OutlinedTextField(
            value = salaryInput,
            onValueChange = { salaryInput = it },
            label = { Text("Gehalt") }
        )

        Button(onClick = {
            if (salaryInput.isNotBlank()) {
                viewModelSt.saveSalary(salaryInput.toInt())

            }
        }) {
            Text("Speichern")
        }
    }
}