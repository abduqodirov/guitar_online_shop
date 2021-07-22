package com.abduqodirov.guitaronlineshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.FragmentSubmitNewProductBinding
import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.model.Product
import com.abduqodirov.guitaronlineshop.model.SendingProduct
import com.abduqodirov.guitaronlineshop.network.Status.*
import com.abduqodirov.guitaronlineshop.viewmodel.SubmitProductViewModel

private const val EDITTEXT_NAME_POSITION = 0
private const val EDITTEXT_PRICE_POSITION = 1
private const val EDITTEXT_DESC_POSITION = 2

private const val MINIMUM_DESC_LENGTH = 10

class SubmitNewProductFragment : Fragment() {

    private var _binding: FragmentSubmitNewProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubmitProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSubmitNewProductBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.formInputsValidationLive.observe(viewLifecycleOwner, Observer {

            binding.submitProductSendBtn.isEnabled = !it.contains(false)

        })

        binding.submitProductNameEdt.addTextChangedListener {

            validateEditText(EDITTEXT_NAME_POSITION, it.toString(), ::isValidName)
        }

        binding.submitProductPriceEdt.addTextChangedListener {

            validateEditText(EDITTEXT_PRICE_POSITION, it.toString(), ::isValidPrice)
        }

        binding.submitProductDescEdt.addTextChangedListener {
            validateEditText(EDITTEXT_DESC_POSITION, it.toString(), ::isValidDesc)
        }

        binding.submitProductSendBtn.setOnClickListener {

            val name = binding.submitProductNameEdt.text.toString()
            val price = binding.submitProductPriceEdt.text.toString().toDouble()
            val desc = binding.submitProductDescEdt.text.toString()

            val sendingProduct = SendingProduct(
                name = name,
                price = price,
                description = desc,
                photos = listOf(""),
                comments = listOf(""),
                rating = listOf(2.5, 4.5)
            )

            viewModel.sendProduct(sendingProduct)

        }

        viewModel.sendingProduct.observe(viewLifecycleOwner, {
            it.let { response ->

                when (response.status) {

                    LOADING -> {
                        binding.submitProductSendBtn.visibility = View.INVISIBLE
                        binding.submitProductProgressBar.visibility = View.VISIBLE
                        binding.submitProductMessageTxt.visibility = View.INVISIBLE
                    }

                    SUCCESS -> {
                        binding.submitProductProgressBar.hide()
                        binding.submitProductMessageTxt.text =
                            getString(R.string.successfully_uploaded)
                        binding.submitProductsProductDetailsBtn.visibility = View.VISIBLE

                        binding.submitProductsProductDetailsBtn.setOnClickListener {
                            response.data?.let { product -> navigateToProductDetailsScreen(product) }
                        }
                    }

                    ERROR -> {
                        binding.submitProductProgressBar.hide()
                        binding.submitProductSendBtn.visibility = View.VISIBLE
                        binding.submitProductMessageTxt.text =
                            getString(R.string.error_on_sending_product)
                        binding.submitProductMessageTxt.visibility = View.VISIBLE
                    }

                }

            }
        })
    }

    private fun navigateToProductDetailsScreen(product: FetchingProduct) {
        findNavController().navigate(
            SubmitNewProductFragmentDirections.actionSubmitNewProductFragmentToProductDetailsFragment(
                product.id
            )
        )
    }

    private fun validateEditText(
        position: Int,
        text: String,
        validationLogic: (text: String) -> Boolean
    ) {
        val oldValidation = viewModel.formInputsValidationLive.value
        val result = validationLogic(text)
        oldValidation?.set(position, result)
        viewModel.formInputsValidationLive.value = oldValidation

    }


    private fun isValidName(name: String) = name.isNotEmpty()

    private fun isValidPrice(text: String): Boolean {
        if (text.isEmpty()) {
            return false
        }
        val price = text.toDouble()

        return price > 0
    }

    private fun isValidDesc(desc: String): Boolean {
        return desc.isNotEmpty() && desc.length > MINIMUM_DESC_LENGTH
    }


    companion object {
        @JvmStatic
        fun newInstance() = SubmitNewProductFragment()
    }
}