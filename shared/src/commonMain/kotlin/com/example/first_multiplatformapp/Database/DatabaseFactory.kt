package com.example.first_multiplatformapp.Database

import com.example.AppDatabase

fun createDatabase(driverFactory: DatabaseDriverFactory): AppDatabase {
    val driver = driverFactory.createDriver()
    return AppDatabase(driver)
}
