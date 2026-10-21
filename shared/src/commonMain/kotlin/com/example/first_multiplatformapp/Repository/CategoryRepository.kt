package com.example.first_multiplatformapp.Repository

import com.example.AppDatabase
import com.example.Categories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList

class CategoryRepository(database: AppDatabase) {

    private var queries = database.categoriesQueries


    fun getAllCategories(): Flow<List<Categories>> {
        return queries.selectAllCategories().asFlow().mapToList(Dispatchers.Default)
    }

    suspend fun insertCategory(name: String){
        withContext(Dispatchers.IO) {
            queries.insertCategory(name)
        }
    }
}