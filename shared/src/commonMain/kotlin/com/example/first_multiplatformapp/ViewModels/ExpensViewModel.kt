package com.example.first_multiplatformapp.ViewModels

import com.example.Categories
import com.example.ExpensBase
import com.example.first_multiplatformapp.Repository.CategoryRepository
import com.example.first_multiplatformapp.Repository.ExpenseRepository
import com.example.first_multiplatformapp.statistics.CategoryChartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

class ExpensViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) {


    private val scope = CoroutineScope(Dispatchers.Default)


    private val _expenses = MutableStateFlow<List<ExpensBase>>(emptyList())
    val expenses: StateFlow<List<ExpensBase>> = _expenses.asStateFlow()

    private val _periodExpenses =
        MutableStateFlow<List<ExpensBase>>(emptyList())

    val periodExpenses: StateFlow<List<ExpensBase>> =
        _periodExpenses.asStateFlow()




    private val _categoryChartData =
        MutableStateFlow<List<CategoryChartItem>>(emptyList())

    val categoryChartData: StateFlow<List<CategoryChartItem>> =
        _categoryChartData.asStateFlow()

    private fun updateCategoryChart(
        expenses: List<ExpensBase>
    ) {

        val grouped = expenses
            .groupBy { it.category }
            .map { (category, expenses) ->

                CategoryChartItem(
                    category = category,
                    amount = expenses.sumOf {
                        it.sume!!.toDouble()
                    }
                )
            }
            .sortedByDescending { it.amount }

        _categoryChartData.value = grouped
    }

    fun loadCategoryChartLast7Days() {

        scope.launch {

            val today =
                Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                )

            val weekAgo =
                today.minus(
                    DatePeriod(days = 6)
                )

            val startDate =
                "${weekAgo.year}-${
                    weekAgo.monthNumber.toString().padStart(2, '0')
                }-${
                    weekAgo.dayOfMonth.toString().padStart(2, '0')
                }"

            val endDate =
                "${today.year}-${
                    today.monthNumber.toString().padStart(2, '0')
                }-${
                    today.dayOfMonth.toString().padStart(2, '0')
                }"

            expenseRepository
                .getExpensesBetweenDates(
                    startDate,
                    endDate
                )
                .collect { expenses ->

                    updateCategoryChart(expenses)

                }
        }
    }

    fun loadCategoryChartCurrentMonth() {

        scope.launch {

            val month = getCurrentMonth()

            expenseRepository
                .getMonthExpenses(month)
                .collect { expenses ->

                    updateCategoryChart(expenses)

                }
        }
    }







    val categories: StateFlow<List<Categories>> =
        categoryRepository.getAllCategories()
            .stateIn(
                scope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    // ------------------ UI STATE ------------------

    private val _uiState = MutableStateFlow("Idle")
    val uiState: StateFlow<String> = _uiState

    // ------------------ ACTIONS ------------------
    fun resetState(){
        _uiState.value = "Idle"
    }

    private val _filter = MutableStateFlow(FilterType.LAST10)
    val filter = _filter.asStateFlow()



    fun addExpense(sume: String, category: String, date: String) {
        scope.launch {
            if (sume.isBlank() || category.isBlank() || date.isBlank()) {
                _uiState.value = "Error"
                return@launch
            }

            try {
                expenseRepository.addExpense(
                    sume = sume.toLong(),
                    category = category,
                    date = date
                )
                _uiState.value = "Success"
            } catch (e: Exception) {
                _uiState.value = "Error"
            }
        }
    }



    fun addCategory(name: String, onDone: () -> Unit) {
        scope.launch {
            if (name.isBlank()) return@launch

            categoryRepository.insertCategory(name)


            withContext(Dispatchers.Main) {
                onDone()
            }
        }
    }

    fun changeFilter(
        newFilter: FilterType
    ) {


        _filter.value = newFilter


        loadExpenses()

    }


    init {
        loadExpenses()
    }

    private fun loadExpenses(){


        scope.launch {


            when(_filter.value){
                FilterType.LAST10 -> {
                    expenseRepository
                        .getLast10Expenses()
                        .collect {
                            _expenses.value = it
                        }

                }
                // -----------------------------
                // ТИЖДЕНЬ
                // -----------------------------

                FilterType.WEEK -> {
                    val date = getWeekDate()

                    expenseRepository
                        .getWeekExpenses(date)
                        .collect {
                            _expenses.value = it
                        }


                }


                // -----------------------------
                // МІСЯЦЬ
                // -----------------------------

                FilterType.MONTH -> {


                    val month =
                        getCurrentMonth()



                    expenseRepository
                        .getMonthExpenses(month)
                        .collect {


                            _expenses.value = it


                        }
                }
            }
        }
    }



    private fun getWeekDate(): String {
        val today =
            Clock.System.todayIn(
                TimeZone.currentSystemDefault()
            )
        val weekAgo =
            today.minus(
                DatePeriod(days = 7)
            )


        return "${weekAgo.year}-${
            weekAgo.monthNumber
                .toString()
                .padStart(2, '0')
        }-${
            weekAgo.dayOfMonth
                .toString()
                .padStart(2, '0')
        }"
    }





    // =================================================
    // ПОТОЧНИЙ МІСЯЦЬ
    // =================================================


    private fun getCurrentMonth(): String {
        val today =
            Clock.System.todayIn(
                TimeZone.currentSystemDefault()
            )


        return "${today.year}-${
            today.monthNumber
                .toString()
                .padStart(2,'0')
        }"

    }



}

enum class FilterType {
    LAST10,
    WEEK,
    MONTH
}