package com.example.first_multiplatformapp.XML

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.statistics.DonutChart
import com.example.first_multiplatformapp.statistics.DonutItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    onBack: () -> Unit,
    viewModelEx: ExpensViewModel
) {
    val categoryData by viewModelEx.categoryChartData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Графики")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Витрати за категоріями",
                    fontSize = 24.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = true,
                        onClick = {
                            viewModelEx.loadCategoryChartLast7Days()
                        },
                        label = {
                            Text("7 днів")
                        }
                    )

                    FilterChip(
                        selected = false,
                        onClick = {
                            viewModelEx.loadCategoryChartCurrentMonth()
                        },
                        label = {
                            Text("Місяць")
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                val donutItems = categoryData.mapIndexed { index, item ->
                    DonutItem(
                        category = item.category,
                        amount = item.amount,
                        color = getChartColor(index)
                    )
                }

                DonutChart(
                    items = donutItems,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModelEx.loadCategoryChartLast7Days()
    }
}

private fun getChartColor(
    index: Int
): Color {
    val colors = listOf(
        Color(0xFF5F0A87),
        Color(0xFF20BF55),
        Color(0xFFEC9F05),
        Color(0xFFF53844),
        Color(0xFF2196F3),
        Color(0xFF9C27B0),
        Color(0xFF009688)
    )

    return colors[index % colors.size]
}
