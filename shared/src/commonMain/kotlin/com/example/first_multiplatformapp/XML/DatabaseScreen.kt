package com.example.first_multiplatformapp.XML

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.ViewModels.FilterType
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel

@Composable
fun DatabaseScreen(
    viewModelEx: ExpensViewModel,
    viewModelSt: SettingsViewModel,
    onChartsClick: () -> Unit
) {
    val expenses by viewModelEx.expenses.collectAsState()
    val balance by viewModelSt.balance.collectAsState()
    val currentFilter by viewModelEx.filter.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onChartsClick
            ) {
                Text("Statistik")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Kontostand: $balance",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 25.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentFilter == FilterType.LAST10,
                    onClick = {
                        viewModelEx.changeFilter(FilterType.LAST10)
                    },
                    label = {
                        Text("Letzte 10")
                    }
                )

                FilterChip(
                    selected = currentFilter == FilterType.WEEK,
                    onClick = {
                        viewModelEx.changeFilter(FilterType.WEEK)
                    },
                    label = {
                        Text("Woche")
                    }
                )

                FilterChip(
                    selected = currentFilter == FilterType.MONTH,
                    onClick = {
                        viewModelEx.changeFilter(FilterType.MONTH)
                    },
                    label = {
                        Text("Monat")
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = expenses,
                    key = { it.id }
                ) { expense ->
                    ExpenseCard(expense)
                }
            }
        }
    }
}
