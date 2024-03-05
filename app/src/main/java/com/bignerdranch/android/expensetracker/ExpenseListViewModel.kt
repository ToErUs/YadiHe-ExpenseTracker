package com.bignerdranch.android.expensetracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ExpenseListViewModel : ViewModel() {
    private val expenseRepository = ExpenseRepository.get()

    private val _crimes: MutableStateFlow<List<Expense>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Expense>>
        get() = _crimes.asStateFlow()

    init {

        viewModelScope.launch {
            //crimeRepository.insertCrime(Crime(id = UUID.randomUUID(), title = "title", Date(), isSolved = false, newsTitle = "title", newsText = "text"))
            expenseRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

    fun addEmptyCrime(onCrimeAdded: (UUID) -> Unit) {
        val newID = UUID.randomUUID()
        viewModelScope.launch {
            expenseRepository.insertExpense(Expense(id = newID, title = "title", Date(), amount = 0, expenseType = 0))
            Log.d("database operation", "insert done")
            // Notify the caller that the crime has been added
            onCrimeAdded(newID)
        }
    }
}
