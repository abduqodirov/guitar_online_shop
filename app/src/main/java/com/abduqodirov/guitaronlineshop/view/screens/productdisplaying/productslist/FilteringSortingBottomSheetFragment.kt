package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.DialogFragmentSortingAndFilteringBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilteringSortingBottomSheetFragment : BottomSheetDialogFragment() {

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

        binding.filteringApplyBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(onLowPriceChangeSetListener: () -> Unit) {

            FilteringSortingBottomSheetFragment()
        }
    }
}
