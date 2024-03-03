package com.bignerdranch.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import java.util.Date

@Entity
data class Expense(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val amount: Int,
    val expenseType:Int
)
