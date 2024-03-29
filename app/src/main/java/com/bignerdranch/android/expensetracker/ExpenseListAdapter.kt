package com.bignerdranch.android.expensetracker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.expensetracker.databinding.ListItemExpenseBinding
import java.util.Date
import java.util.UUID

class ExpenseHolder(
    private val binding: ListItemExpenseBinding
) : RecyclerView.ViewHolder(binding.root) {

    val typeToIntMap = mapOf(
        // Food, Entertainment, Housing, Utilities, Fuel, Automotive, Misc
        "Food" to 0,
        "Entertainment" to 1,
        "Housing" to 2,
        "Utilities" to 3,
        "Fuel" to 4,
        "Automotive" to 5,
        "Misc" to 6
    )
    fun bind(expense: Expense, onCrimeClicked: (crimeId: UUID) -> Unit) {
        val intToTypeMap = typeToIntMap.entries.associateBy({ it.value }) { it.key }

        binding.expenseType.text = intToTypeMap[expense.expenseType]
        binding.crimeDate.text = expense.date.toString()
        binding.amount.text= expense.amount.toString()


        binding.root.setOnClickListener {
            onCrimeClicked(expense.id)
        }


    }
}

class ExpenseListAdapter(
    private val expenses: List<Expense>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<ExpenseHolder>() {

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
    ): ExpenseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemExpenseBinding.inflate(inflater, parent, false)
        return ExpenseHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        val crime = filteredItems[position]
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = filteredItems.size
}
