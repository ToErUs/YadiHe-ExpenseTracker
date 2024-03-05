package com.bignerdranch.android.criminalintent

import android.app.Application

class ExpenseTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ExpenseRepository.initialize(this)
    }
}
