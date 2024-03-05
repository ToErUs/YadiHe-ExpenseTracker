package com.bignerdranch.android.expensetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.expensetracker.Expense

@Database(entities = [Expense::class], version = 8)
@TypeConverters(ExpenseTypeConverters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun crimeDao(): ExpenseDao
}
