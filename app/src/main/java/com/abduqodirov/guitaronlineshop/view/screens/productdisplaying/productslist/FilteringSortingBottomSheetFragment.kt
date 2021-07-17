package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.DialogFragmentSortingAndFilteringBinding
import com.abduqodirov.guitaronlineshop.view.model.SortOrderOption
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.adapters.SortOrderOptionsAdapter
import com.abduqodirov.guitaronlineshop.view.util.orders
import com.abduqodirov.guitaronlineshop.view.util.sortByOptions
import com.abduqodirov.guitaronlineshop.view.util.sortOrderOptions
import com.abduqodirov.guitaronlineshop.view.util.toIntOrZeroIfEmpty
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class FilteringSortingBottomSheetFragment(
    private val listener: SortingAndFilteringChangeListener
) : BottomSheetDialogFragment() {

    private var _binding: DialogFragmentSortingAndFilteringBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FilterDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentSortingAndFilteringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedSortBy = sortByOptions[0]
        var selectedOrderOfSort = orders[0]

        setSortAndOrderSpinnerListener { selectedOption ->

            selectedSortBy = selectedOption.sortBy
            selectedOrderOfSort = selectedOption.orderBy
        }

        binding.filteringApplyBtn.setOnClickListener {

            try {
                val sortingFilteringFields = SortingFilteringFields(
                    lowPrice = binding.filteringLowPriceEdt.text.toString().toIntOrZeroIfEmpty(),
                    highPrice = binding.filteringHighPriceEdt.text.toString().toIntOrZeroIfEmpty(),
                    sortBy = selectedSortBy,
                    nameFilter = "",
                    order = selectedOrderOfSort
                )

                if (sortingFilteringFields.arePricesValid()) {
                    listener.onFieldsChangeListener(sortingFilteringFields)
                    dismiss()
                } else {
                    showErrorWithSnackBar(getString(R.string.low_high_balance_error))
                }
            } catch (number: NumberFormatException) {
                showErrorWithSnackBar(getString(R.string.int_limit))
            }
        }
    }

    private fun showErrorWithSnackBar(msg: String) {
        Snackbar.make(binding.dialogFilterRoot, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun setSortAndOrderSpinnerListener(sortSelectedListener: (choice: SortOrderOption) -> Unit) {
        val sortAdapter = SortOrderOptionsAdapter(
            requireContext(),
            sortOrderOptions
        )

        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dialogFilterSortSpinner.adapter = sortAdapter

        binding.dialogFilterSortSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sortSelectedListener(sortOrderOptions[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(
            onSortingFilteringFieldsChangeListener: SortingAndFilteringChangeListener
        ): FilteringSortingBottomSheetFragment {
            return FilteringSortingBottomSheetFragment(onSortingFilteringFieldsChangeListener)
        }
    }
}
