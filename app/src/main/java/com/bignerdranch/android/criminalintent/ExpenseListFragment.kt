package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentExpenseListBinding
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val expenseListViewModel: ExpenseListViewModel by viewModels()

    val typeToIntMap = mapOf(
        "Theft" to 0,
        "Assault" to 1,
        "Vandalism" to 2,
        "Burglary" to 3
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        binding.spinnerTypeFilter.setSelection(0)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.buttonAdd.setOnClickListener{
            // Handle button click event
            Toast.makeText(requireContext(), "Button clicked", Toast.LENGTH_SHORT).show()
            expenseListViewModel.addEmptyCrime { newID ->
                // Handle the newly inserted crime with its UUID
                Log.d("Crime added", "UUID: $newID")
                // Now you can use the newID as needed
                findNavController().navigate(
                    ExpenseListFragmentDirections.showCrimeDetail(newID)
                )
            }

        }
        binding.textInputDate.setOnClickListener{

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)



            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    // Update the TextInputEditText with the selected date
                    binding.textInputDate.setText("$year-${monthOfYear + 1}-$dayOfMonth")

                    val selectedDate = Calendar.getInstance().apply {
                        set(year, monthOfYear, dayOfMonth)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    val startTime = selectedDate.time
                    selectedDate.apply {
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 999)
                    }

                    binding.spinnerTypeFilter.setSelection(0)
                    val endTime = selectedDate.time
                    val adapter = binding.crimeRecyclerView.adapter as? CrimeListAdapter
                    (binding.crimeRecyclerView.adapter as? CrimeListAdapter)?.filterByDate(startTime,endTime)
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }
        binding.spinnerTypeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.textInputDate.setText("")
                val selectedType = parent?.getItemAtPosition(position).toString()

                // Call a function to filter data based on the selected type
                val adapter = binding.crimeRecyclerView.adapter as? CrimeListAdapter
                if(selectedType=="All"){
                    (binding.crimeRecyclerView.adapter as? CrimeListAdapter)?.removeFilter()
                }else{
                    (binding.crimeRecyclerView.adapter as? CrimeListAdapter)?.filterByType(typeToIntMap[selectedType]?:0)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where no item is selected (if needed)
            }
        }

        binding.clearFilter.setOnClickListener {
            binding.textInputDate.setText("") // Set the text to empty string
            val adapter = binding.crimeRecyclerView.adapter as? CrimeListAdapter
            (binding.crimeRecyclerView.adapter as? CrimeListAdapter)?.removeFilter()
            binding.spinnerTypeFilter.setSelection(0)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                expenseListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter =
                        CrimeListAdapter(crimes) { crimeId ->
                            findNavController().navigate(
                                ExpenseListFragmentDirections.showCrimeDetail(crimeId)
                            )
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
