package com.example.first_multiplatformapp.XML


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ExpensBase
import com.example.first_multiplatformapp.ViewModels.ExpensViewModel
import com.example.first_multiplatformapp.ViewModels.SettingsViewModel

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import com.example.first_multiplatformapp.ViewModels.FilterType

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import com.example.first_multiplatformapp.statistics.CategoryChartItem
import com.example.first_multiplatformapp.statistics.DonutChart
import com.example.first_multiplatformapp.statistics.DonutItem


@Composable
fun App(viewModel: ExpensViewModel, viewModelSt: SettingsViewModel) {

    val isFirstLaunch by viewModelSt.isFirstLaunch.collectAsState()

    if (isFirstLaunch) {
        SalaryScreen(viewModelSt)
    } else {
        MainApp(viewModel, viewModelSt)
    }
}


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
                onBack = {
                    showCharts = false
                },
                viewModelEx = viewModelEx

            )

        } else {

            Scaffold(
                bottomBar = {
                    NavigationBar {

                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = {
                                selectedTab = 0
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Главная"
                                )
                            },
                            label = {
                                Text("Головна")
                            }
                        )

                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = {
                                selectedTab = 1
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Статистика"
                                )
                            },
                            label = {
                                Text("Статистика")
                            }
                        )

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = {
                                selectedTab = 2
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Профиль"
                                )
                            },
                            label = {
                                Text("Профіль")
                            }
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

                        0 -> {
                            MainScreen(
                                viewModelEx,
                                viewModelSt
                            )
                        }

                        1 -> {
                            DatabaseScreen(
                                viewModelEx,
                                viewModelSt,
                                onChartsClick = {
                                    showCharts = true
                                }
                            )
                        }

                        2 -> {
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}



// Окремі функції для кожного екрана
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(viewModel: ExpensViewModel, viewModelSt: SettingsViewModel) {


    // база данных
    var sume by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    var customCategory by remember { mutableStateOf("") }


    //Kategorii
    var newCategoryName by remember { mutableStateOf("") }
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val categoriesList by viewModel.categories.collectAsState()


    // Робота з датою
    val today = Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    var isShowDatePicker by remember { mutableStateOf(false) }



    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    val salary by viewModelSt.salary.collectAsState()
    // val bonus by viewModelSt.bonus.collectAsState()
    var inputBonus by remember { mutableStateOf("") }



    LaunchedEffect(state){
        when(state){
            "Success" ->{
                sume = ""
                category = ""
                customCategory = ""
                date = ""
            }
            "Error" ->{
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
                    title = { Text("Geld Assistent", fontWeight = FontWeight.Bold, fontSize = 35.sp) },
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { isSheetOpen = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    // ДОБАВЛЯЕМ ЭТУ СТРОКУ:
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Category")
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


             Text("Gehalt: $salary", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
             //  Button()
             /*
             TextField(
                 value = inputBonus,
                 onValueChange = {                // тільки цифри
                     if (it.all { char -> char.isDigit() }) {
                         inputBonus = it
                     }},
                 label = { Text("Bonus eingeben") },
                 keyboardOptions = KeyboardOptions(
                     keyboardType = KeyboardType.Number
                 ),
                 modifier = Modifier.fillMaxWidth()
             )
             */



             Button(onClick = {viewModelSt.clearSalary()}){
                 Text("Delete Gehalt")
             }



             Spacer(modifier = Modifier.height(50.dp))



             Card(
                 colors = CardDefaults.cardColors(
                     containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(20.dp)
             ) {
                 Column(modifier = Modifier.padding(25.dp).verticalScroll(rememberScrollState())) {

                     OutlinedTextField(
                         value = sume,
                         modifier = Modifier.fillMaxWidth(),
                         singleLine = true,
                         onValueChange = { input ->
                             // Условие: только цифры И длина не больше 10 символов
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

                     FlowRow(
                         horizontalArrangement = Arrangement.spacedBy(8.dp),
                         verticalArrangement = Arrangement.spacedBy(8.dp)
                     ) {

                         categoriesList.forEach { categoryItem ->

                             FilterChip(
                                 selected = category == categoryItem.name,
                                 onClick = { category = categoryItem.name },

                                 label = {
                                     Text(categoryItem.name)
                                 }
                             )

                         }
                         FilterChip(
                             selected = category == "CUSTOM",
                             onClick = { category = "CUSTOM" },
                             label = { Text("Andere") }
                         )
                     }


                     if (category == "CUSTOM") {
                         Spacer(modifier = Modifier.height(12.dp))

                         OutlinedTextField(
                             value = customCategory,
                             onValueChange = { customCategory = it },
                             label = { Text("Verwalten Sie Ihre Kategorie") },
                             modifier = Modifier.fillMaxWidth()
                         )
                     }

                     Row(
                         modifier = Modifier.fillMaxWidth().padding(8.dp),
                         horizontalArrangement = Arrangement.spacedBy(8.dp)
                     ) {
                         val buttonModifier = Modifier.weight(1f)

                         Button(onClick = { date = today.toString() }, modifier = buttonModifier,
                             contentPadding = PaddingValues(0.dp)
                         ) {
                             Text("Heute", maxLines = 1)
                         }
                         Button(onClick = { date = yesterday.toString() }, modifier = buttonModifier) {
                             Text("Gestern", maxLines = 1)
                         }
                         IconButton(
                             onClick = { isShowDatePicker = true },
                             modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
                         ) {
                             Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
                         }
                     }
                     Spacer(modifier = Modifier.height(20.dp))

                     Text(
                         text = if (date.isEmpty()) "" else "$date",
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

                             // очистка UI
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

            if (isSheetOpen){
              ModalBottomSheet(
                  onDismissRequest = {isSheetOpen = false},
                  sheetState = sheetState
              ){
                  Column(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(16.dp)
                          .navigationBarsPadding()
                  ) {
                      Text("New Category",
                          style = MaterialTheme.typography.headlineSmall,
                          modifier = Modifier.align(Alignment.CenterHorizontally)
                      )

                      Spacer(modifier = Modifier.height(15.dp))

                      OutlinedTextField(
                          value = newCategoryName,
                          onValueChange = { newCategoryName = it },
                          label = { Text("Name") },
                          modifier = Modifier.fillMaxWidth(),
                          singleLine = true
                      )

                      Spacer(modifier = Modifier.height(20.dp))

                      Button(
                          modifier = Modifier.fillMaxWidth(),
                          enabled = newCategoryName.isNotBlank(),
                          onClick = {
                              viewModel.addCategory(newCategoryName) {
                                  newCategoryName = ""
                                  isSheetOpen = false
                              }
                          }

                      ) {
                          Text("Hinzufügen")
                      }
                      Spacer(modifier = Modifier.height(20.dp))




                  }
              }
            }


            if (isShowDatePicker) {

                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { isShowDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            val selectedDate = datePickerState.selectedDateMillis
                            if (selectedDate != null) {
                                val localDate = Instant
                                    .fromEpochMilliseconds(selectedDate)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .date

                                date = localDate.toString()
                            }
                            isShowDatePicker = false
                        }) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }




        }
}

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
                Text("Графики")
            }

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Text(
                text = "Balance: $balance",
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
                        Text("Останні 10")
                    }
                )

                FilterChip(
                    selected = currentFilter == FilterType.WEEK,
                    onClick = {
                        viewModelEx.changeFilter(FilterType.WEEK)
                    },
                    label = {
                        Text("Тиждень")
                    }
                )

                FilterChip(
                    selected = currentFilter == FilterType.MONTH,
                    onClick = {
                        viewModelEx.changeFilter(FilterType.MONTH)
                    },
                    label = {
                        Text("Місяць")
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


@Composable
fun ExpenseCard(
    expense: ExpensBase
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = expense.category,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Сума: ${expense.sume} грн"
            )

            Text(
                text = expense.date
            )

        }

    }
}

@Composable
fun ProfileScreen() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Екран налаштувань профілю")
    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    onBack: () -> Unit,
    viewModelEx: ExpensViewModel
) {

    val categoryData by
    viewModelEx.categoryChartData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

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
                    .padding(16.dp)
            ) {

                Text(
                    text = "Витрати за категоріями",
                    fontSize = 24.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =========================
                // ПЕРИОД
                // =========================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    FilterChip(
                        selected = true,
                        onClick = {
                            viewModelEx
                                .loadCategoryChartLast7Days()
                        },
                        label = {
                            Text("7 днів")
                        }
                    )

                    FilterChip(
                        selected = false,
                        onClick = {
                            viewModelEx
                                .loadCategoryChartCurrentMonth()
                        },
                        label = {
                            Text("Місяць")
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // =========================
                // DONUT CHART
                // =========================

                val donutItems =
                    categoryData.mapIndexed { index, item ->

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

    // Загружаем 7 дней
    // при первом открытии экрана

    LaunchedEffect(Unit) {

        viewModelEx
            .loadCategoryChartLast7Days()
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