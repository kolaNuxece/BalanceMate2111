package com.example.first_multiplatformapp.ViewModels

import androidx.compose.runtime.Immutable
import com.example.first_multiplatformapp.Repository.ExpenseRepository
import com.example.first_multiplatformapp.Repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn


class SettingsViewModel(private val settingsrepository: SettingsRepository,
                        private val expenseRepository: ExpenseRepository,
                        private val scope: CoroutineScope
) {


    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    private val _salary = MutableStateFlow(0)
    val salary: StateFlow<Int> = _salary


    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> = _balance


    init {
        LoadData()
    }
    fun LoadData(){
        val savedSalary = settingsrepository.getSalary()
        _salary.value = savedSalary
        _isFirstLaunch.value = savedSalary == 0

        scope.launch {
            calculateBalance()
        }
    }


    suspend fun calculateBalance(){

        val today = Clock.System
            .todayIn(TimeZone.currentSystemDefault())

        val month =
            "${today.year}-${today.monthNumber.toString().padStart(2, '0')}"


        val spent =
            expenseRepository
                .getMonthSum(month)


        val result =
            _salary.value - spent


        _balance.value = result.toInt()
    }

    // Логіка перевірки місяця

    fun saveSalary(value: Int){
        settingsrepository.saveSalary(value)
        _salary.value = value
        _isFirstLaunch.value = false
    }


    private fun getCurrentMonthString(): String {
        // Якщо використовуєш kotlinx-datetime:
         val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
         return "${now.year}-${now.monthNumber}"
      //  return "2024-05" // Тимчасова заглушка
    }


    fun clearSalary(){
        settingsrepository.clearSalary()

        _isFirstLaunch.value = true
    }

}