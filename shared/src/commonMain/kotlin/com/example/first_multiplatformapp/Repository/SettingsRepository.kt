package com.example.first_multiplatformapp.Repository

import com.russhwolf.settings.Settings

class SettingsRepository(private val settings: Settings) {

    fun saveSalary(value: Int){
        settings.putInt("salary", value)
    }

    fun getSalary(): Int {
        return settings.getInt("salary", 0)
    }


    fun saveLastMonth(month: String) {
        settings.putString("last_month", month)
    }


    fun getLastMonth(): String {
        return settings.getString("last_month", "")
    }

    fun clearSalary(){
        settings.remove("salary")
        settings.remove("bonus")
        settings.remove("last_month")
    }
}


