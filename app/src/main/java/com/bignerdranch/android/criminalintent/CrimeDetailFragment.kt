package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.databinding.FragmentExpenseDetailBinding
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

private const val TAG = "CrimeDetailFragment"

class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    val typeToIntMap = mapOf(
        "Theft" to 0,
        "Assault" to 1,
        "Vandalism" to 2,
        "Burglary" to 3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val expense = Expense(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            amount = 0,
            expenseType = 0
        )

        Log.d(TAG, "The crime ID is: ${args.crimeId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentExpenseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.delete.setOnClickListener{
            Toast.makeText(requireContext(), "Delete clicked", Toast.LENGTH_SHORT).show()
            deleteCrimeAndNavigateBack()
        }

        binding.spinnerCrimeType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem:String = parent?.getItemAtPosition(position).toString()
                Log.d("Spiner", "The crime type is: $selectedItem")
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(expenseType = typeToIntMap[selectedItem]?:0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.apply {
            expenseAmount.doOnTextChanged { text, _, _, _ ->
                val amount = if (text.isNullOrEmpty()) {
                    0 // Set amount to 0 if text is null or empty
                } else {
                    Integer.parseInt(text.toString()) // Parse the text to an integer
                }

                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(amount = amount)
                }
            }



            crimeDate.apply {
                isEnabled = false
            }

//            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
//                crimeDetailViewModel.updateCrime { oldCrime ->
//                    oldCrime.copy(isSolved = isChecked)
//                }
//            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.expense.collect { crime ->
                    crime?.let { updateUi(it) }
                }
            }
        }
    }

    private fun deleteCrimeAndNavigateBack() {
        lifecycleScope.launch {
            crimeDetailViewModel.deleteCrime()
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(expense: Expense) {
        binding.apply {
            if(expenseAmount.text.toString()==""){
                expenseAmount.setText(expense.amount.toString())
            }else{
                if (!expenseAmount.text.toString().matches("-?\\d+".toRegex())||Integer.parseInt(expenseAmount.text.toString()) != expense.amount) {

                }
            }

            crimeDate.text = expense.date.toString()
            spinnerCrimeType.setSelection(expense.expenseType)

        }
    }
}
