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
    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            onCrimeClicked(crime.id)
        }

        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeHolder>() {

    private var filteredItems: List<Crime> = crimes

    fun filterByType(type: Int) {
        filteredItems = crimes.filter { item ->
            item.expenseType == type
        }
        notifyDataSetChanged()
    }

    fun filterByDate(startDate: Date, endDate: Date) {
        filteredItems = crimes.filter { item ->
            val itemDate = item.date
            itemDate in startDate..endDate
        }
        notifyDataSetChanged()
        Log.d("filter", "date set to: $startDate")
    }

    fun removeFilter(){
        filteredItems=crimes
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
