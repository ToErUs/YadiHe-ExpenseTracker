package com.bignerdranch.android.criminalintent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import java.util.Date
import java.util.UUID

class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(expense: Expense, onCrimeClicked: (crimeId: UUID) -> Unit) {
        binding.expenseType.text = expense.expenseType.toString()
        binding.crimeDate.text = expense.date.toString()
        binding.amount.text= expense.amount.toString()

        binding.root.setOnClickListener {
            onCrimeClicked(expense.id)
        }


    }
}

class CrimeListAdapter(
    private val expenses: List<Expense>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeHolder>() {

    private var filteredItems: List<Expense> = expenses

    fun filterByType(type: Int) {
        filteredItems = expenses.filter { item ->
            item.expenseType == type
        }
        notifyDataSetChanged()
    }

    fun filterByDate(startDate: Date, endDate: Date) {
        filteredItems = expenses.filter { item ->
            val itemDate = item.date
            itemDate in startDate..endDate
        }
        notifyDataSetChanged()
        Log.d("filter", "date set to: $startDate")
    }

    fun removeFilter(){
        filteredItems=expenses
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = filteredItems[position]
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = filteredItems.size
}
