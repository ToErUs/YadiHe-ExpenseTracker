package com.bignerdranch.android.expensetracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.expensetracker.Expense
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense")
    fun getCrimes(): Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE id=(:id)")
    suspend fun getCrime(id: UUID): Expense

    @Update
    suspend fun updateCrime(expense: Expense)

    @Insert
    suspend fun insertCrime(news: Expense)

    @Query("DELETE FROM expense WHERE id =(:id)")
    suspend fun deleteCrimeById(id: UUID)
}
