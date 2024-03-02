package com.bignerdranch.android.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class CrimeListViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {

        viewModelScope.launch {
            //crimeRepository.insertCrime(Crime(id = UUID.randomUUID(), title = "title", Date(), isSolved = false, newsTitle = "title", newsText = "text"))
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

    fun addEmptyCrime(onCrimeAdded: (UUID) -> Unit) {
        val newID = UUID.randomUUID()
        viewModelScope.launch {
            crimeRepository.insertCrime(Crime(id = newID, title = "title", Date(), isSolved = false, newsTitle = "title", newsText = "text"))
            Log.d("database operation", "insert done")
            // Notify the caller that the crime has been added
            onCrimeAdded(newID)
        }
    }
}
