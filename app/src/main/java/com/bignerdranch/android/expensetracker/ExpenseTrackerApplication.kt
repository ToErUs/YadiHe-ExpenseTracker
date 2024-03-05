package com.bignerdranch.android.expensetracker

import android.app.Application

class ExpenseTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ExpenseRepository.initialize(this)
    }
}
