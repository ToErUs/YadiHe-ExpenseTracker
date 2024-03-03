package com.bignerdranch.android.criminalintent

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.ExpenseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: ExpenseDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ExpenseDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME).fallbackToDestructiveMigration()
        .build()

    fun getCrimes(): Flow<List<Expense>> = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID): Expense = database.crimeDao().getCrime(id)

    fun updateCrime(expense: Expense) {
        coroutineScope.launch {
            database.crimeDao().updateCrime(expense)
        }
    }



    suspend fun insertCrime(expense: Expense) {
        database.crimeDao().insertCrime(expense)
        Log.d("In RP","Crime inserted")
    }

    suspend fun deleteCrime(id: UUID){
        database.crimeDao().deleteCrimeById(id)
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE
                ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
