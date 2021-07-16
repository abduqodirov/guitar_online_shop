package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.DialogFragmentSortingAndFilteringBinding
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import com.abduqodirov.guitaronlineshop.view.util.orders
import com.abduqodirov.guitaronlineshop.view.util.sortByOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

        setSortSpinnerListener { selectedField ->
            selectedSortBy = selectedField
        }

        setOrderSpinnerListener { selectedOrder ->
            selectedOrderOfSort = selectedOrder
        }

        binding.filteringApplyBtn.setOnClickListener {

            // TODO emtpy bo'lganda 0 qo'yvorish kerak
            val sortingFilteringFields = SortingFilteringFields(
                lowPrice = binding.filteringLowPriceEdt.text.toString().toInt(),
                highPrice = binding.filteringHighPriceEdt.text.toString().toInt(),
                sortBy = selectedSortBy,
                nameFilter = "",
                order = selectedOrderOfSort
            )

            listener.onFieldsChangeListener(sortingFilteringFields)
            dismiss()
        }
    }

    private fun setOrderSpinnerListener(orderSelectedListener: (orderBy: String) -> Unit) {
        val orderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            orders
        )

        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.dialogFilterOrderSpinner.adapter = orderAdapter

        binding.dialogFilterOrderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    orderSelectedListener(sortByOptions[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun setSortSpinnerListener(sortSelectedListener: (sort: String) -> Unit) {
        val sortAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortByOptions
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
                    sortSelectedListener(sortByOptions[position])
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
