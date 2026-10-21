package com.example.first_multiplatformapp.Repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.AppDatabase
import com.example.ExpensBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.asFlow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList

class ExpenseRepository(
    database: AppDatabase
) {


    private val queries =
        database.expensQueries


    fun getLast10Expenses(): Flow<List<ExpensBase>> {

        return queries
            .getLast10Expenses()
            .asFlow()
            .mapToList(Dispatchers.Default)

    }



    // ==========================
    // Останній тиждень
    // ==========================

    fun getWeekExpenses(
        date: String
    ): Flow<List<ExpensBase>> {

        return queries
            .getWeekExpenses(date)
            .asFlow()
            .mapToList(Dispatchers.Default)

    }



    // ==========================
    // Останній місяць
    // ==========================

    fun getMonthExpenses(
        month: String
    ): Flow<List<ExpensBase>> {

        return queries
            .getMonthExpenses("$month%")
            .asFlow()
            .mapToList(Dispatchers.Default)

    }



    suspend fun getMonthSum(
        month: String
    ): Long {

        return withContext(Dispatchers.IO) {

            queries
                .getMonthSum("$month%")
                .executeAsOne()

        }.toLong()

    }



    suspend fun addExpense(
        sume: Long,
        category: String,
        date: String
    ) {

        withContext(Dispatchers.IO) {

            queries.insertExpens(
                sume,
                category,
                date
            )

        }

    }

    fun getExpensesBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<ExpensBase>> {
        return queries
            .getExpensesBetweenDates(startDate, endDate)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }
}
