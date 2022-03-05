package com.example.room

import android.app.Application

class employeeApp:Application() {
    val db by lazy { EmployeeDatabase.getInstance(this) }
}