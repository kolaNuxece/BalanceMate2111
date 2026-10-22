package com.example.first_multiplatformapp.XML

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    viewModel: ExpensViewModel,
    viewModelSt: SettingsViewModel
) {
    var sume by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }

    var newCategoryName by remember { mutableStateOf("") }
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val categoriesList by viewModel.categories.collectAsState()

    val today = Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val yesterday = today.minus(1, DateTimeUnit.DAY)
    var isShowDatePicker by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val salary by viewModelSt.salary.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            "Success" -> {
                sume = ""
                category = ""
                customCategory = ""
                date = ""
            }

            "Error" -> {
                snackbarHostState.showSnackbar("Fehler!")
                viewModel.resetState()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Geld Assistent",
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isSheetOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Category"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "Gehalt: $salary",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 22.sp
            )

            Button(
                onClick = { viewModelSt.clearSalary() }
            ) {
                Text("Delete Gehalt")
            }

            Spacer(modifier = Modifier.height(50.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(25.dp)
                ) {
                    OutlinedTextField(
                        value = sume,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 10) {
                                sume = input
                            }
                        },
                        label = { Text("Sume") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CategorySelector(
                        categories = categoriesList,
                        selectedCategory = category,
                        categoryName = { it.name },
                        onCategorySelected = { category = it }
                    )

                    if (category == "CUSTOM") {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customCategory,
                            onValueChange = { customCategory = it },
                            label = { Text("Verwalten Sie Ihre Kategorie") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    DateSelector(
                        today = today.toString(),
                        yesterday = yesterday.toString(),
                        selectedDate = date,
                        onTodayClick = { date = today.toString() },
                        onYesterdayClick = { date = yesterday.toString() },
                        onCalendarClick = { isShowDatePicker = true }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (date.isEmpty()) "" else date,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        onClick = {
                            val finalCategory =
                                if (category == "CUSTOM") customCategory
                                else category

                            viewModel.addExpense(
                                sume,
                                finalCategory,
                                date
                            )

                            sume = ""
                            category = ""
                            customCategory = ""
                            date = ""
                        }
                    ) {
                        Text("Hinzufügen")
                    }
                }
            }
        }

        if (isSheetOpen) {
            AddCategorySheet(
                sheetState = sheetState,
                categoryName = newCategoryName,
                onCategoryNameChange = { newCategoryName = it },
                onDismiss = { isSheetOpen = false },
                onAddCategory = {
                    viewModel.addCategory(newCategoryName) {
                        newCategoryName = ""
                        isSheetOpen = false
                    }
                }
            )
        }

        if (isShowDatePicker) {
            ExpenseDatePickerDialog(
                onDismiss = { isShowDatePicker = false },
                onDateSelected = { selectedDate ->
                    date = selectedDate
                    isShowDatePicker = false
                }
            )
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun <T> CategorySelector(
    categories: List<T>,
    selectedCategory: String,
    categoryName: (T) -> String,
    onCategorySelected: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { categoryItem ->
            val name = categoryName(categoryItem)
            FilterChip(
                selected = selectedCategory == name,
                onClick = { onCategorySelected(name) },
                label = { Text(name) }
            )
        }

        FilterChip(
            selected = selectedCategory == "CUSTOM",
            onClick = { onCategorySelected("CUSTOM") },
            label = { Text("Andere") }
        )
    }
}

@Composable
private fun DateSelector(
    today: String,
    yesterday: String,
    selectedDate: String,
    onTodayClick: () -> Unit,
    onYesterdayClick: () -> Unit,
    onCalendarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val buttonModifier = Modifier.weight(1f)

        Button(
            onClick = onTodayClick,
            modifier = buttonModifier,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Heute", maxLines = 1)
        }

        Button(
            onClick = onYesterdayClick,
            modifier = buttonModifier
        ) {
            Text("Gestern", maxLines = 1)
        }

        IconButton(
            onClick = onCalendarClick,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.primary,
                CircleShape
            )
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCategorySheet(
    sheetState: androidx.compose.material3.SheetState,
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddCategory: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Text(
                "New Category",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = categoryName,
                onValueChange = onCategoryNameChange,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = categoryName.isNotBlank(),
                onClick = onAddCategory
            ) {
                Text("Hinzufügen")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis

                    if (selectedDateMillis != null) {
                        val localDate = Instant
                            .fromEpochMilliseconds(selectedDateMillis)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date

                        onDateSelected(localDate.toString())
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text("OK")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
