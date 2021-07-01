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
import com.abduqodirov.guitaronlineshop.model.SendingProduct
import com.abduqodirov.guitaronlineshop.network.Response
import com.abduqodirov.guitaronlineshop.network.Status.ERROR
import com.abduqodirov.guitaronlineshop.network.Status.LOADING
import com.abduqodirov.guitaronlineshop.network.Status.SUCCESS
import com.abduqodirov.guitaronlineshop.viewmodel.SubmitProductViewModel

private const val EDITTEXT_NAME_POSITION = 0
private const val EDITTEXT_PRICE_POSITION = 1
private const val EDITTEXT_DESC_POSITION = 2

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

        setUpFormValidators()

        setUpViewClickListeners()

        observeSendingProductStatusAndData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeSendingProductStatusAndData() {
        viewModel.sendingProduct.observe(
            viewLifecycleOwner,
            {
                it.let { response ->

                    when (response.status) {

                        LOADING -> {
                            switchUIToLoadingState()
                        }

                        SUCCESS -> {
                            binding.submitProductProgressBar.hide()
                            binding.submitProductMessageTxt.text =
                                getString(R.string.successfully_uploaded)
                            binding.submitProductsProductDetailsBtn.visibility = View.VISIBLE

                            setUpSuccessfullyUploadedButtonListener(response)
                        }

                        ERROR -> {
                            switchUIToErrorState()
                        }
                    }
                }
            }
        )
    }

    private fun setUpFormValidators() {

        viewModel.formInputsValidationLive.observe(
            viewLifecycleOwner,
            Observer {
                binding.submitProductSendBtn.isEnabled = !it.contains(false)
            }
        )

        binding.submitProductNameEdt.addTextChangedListener {

            viewModel.validateEditText(EDITTEXT_NAME_POSITION, it.toString())
        }

        binding.submitProductPriceEdt.addTextChangedListener {

            viewModel.validateEditText(EDITTEXT_PRICE_POSITION, it.toString())
        }

        binding.submitProductDescEdt.addTextChangedListener {
            viewModel.validateEditText(EDITTEXT_DESC_POSITION, it.toString())
        }
    }

    private fun setUpSuccessfullyUploadedButtonListener(response: Response<FetchingProduct>) {
        binding.submitProductsProductDetailsBtn.setOnClickListener {
            response.data?.let { product ->
                navigateToProductDetailsScreen(
                    product
                )
            }
        }
    }

    private fun switchUIToErrorState() {
        binding.submitProductProgressBar.hide()
        binding.submitProductSendBtn.visibility = View.VISIBLE
        binding.submitProductMessageTxt.text =
            getString(R.string.error_on_sending_product)
        binding.submitProductMessageTxt.visibility = View.VISIBLE
    }

    private fun switchUIToLoadingState() {
        binding.submitProductProgressBar.visibility = View.VISIBLE

        binding.submitProductSendBtn.visibility = View.INVISIBLE
        binding.submitProductMessageTxt.visibility = View.INVISIBLE
    }

    private fun setUpViewClickListeners() {
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
    }

    private fun navigateToProductDetailsScreen(product: FetchingProduct) {
        findNavController().navigate(
            SubmitNewProductFragmentDirections.actionSubmitNewProductFragmentToProductDetailsFragment(
                product.id
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubmitNewProductFragment()
    }
}
