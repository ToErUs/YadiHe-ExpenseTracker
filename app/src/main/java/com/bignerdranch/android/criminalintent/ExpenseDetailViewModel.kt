package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val expenseRepository = ExpenseRepository.get()

    private val _expense: MutableStateFlow<Expense?> = MutableStateFlow(null)
    val expense: StateFlow<Expense?> = _expense.asStateFlow()

    init {
        viewModelScope.launch {
            _expense.value = expenseRepository.getCrime(crimeId)
        }
    }

    fun updateCrime(onUpdate: (Expense) -> Expense) {
        _expense.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    suspend  fun deleteCrime(){
        _expense.value?.id?.let { expenseRepository.deleteCrime(it) }

    }

    override fun onCleared() {
        super.onCleared()
        expense.value?.let { expenseRepository.updateCrime(it) }
    }
}

class CrimeDetailViewModelFactory(
    private val crimeId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}
