package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProduct
import com.abduqodirov.guitaronlineshop.data.network.Response
import com.abduqodirov.guitaronlineshop.databinding.FragmentSubmitNewProductBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import javax.inject.Inject

private const val EDITTEXT_NAME_POSITION = 0
private const val EDITTEXT_PRICE_POSITION = 1
private const val EDITTEXT_DESC_POSITION = 2

class SubmitNewProductFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentSubmitNewProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SubmitProductViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as ShopApplication).appComponent.inject(this)
    }

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
        viewModel.sentProduct.observe(
            viewLifecycleOwner,
            {

                it.let { response ->

                    when (response) {

                        is Response.Loading -> {
                            switchUIToLoadingState()
                        }

                        is Response.Success -> {
                            binding.submitProductProgressBar.hide()
                            binding.submitProductMessageTxt.text =
                                getString(R.string.successfully_uploaded)
                            binding.submitProductsProductDetailsBtn.visibility = View.VISIBLE

                            setUpSuccessfullyUploadedButtonListener(response.data)
                        }

                        is Response.Failure -> {
                            switchUIToErrorState()
                        }
                    }
                }
            }
        )
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

    private fun setUpSuccessfullyUploadedButtonListener(product: FetchingProduct) {
        binding.submitProductsProductDetailsBtn.setOnClickListener {

            navigateToProductDetailsScreen(
                product
            )
        }
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

    private fun setUpViewClickListeners() {
        binding.submitProductSendBtn.setOnClickListener {

            val name = binding.submitProductNameEdt.text.toString()
            val price = binding.submitProductPriceEdt.text.toString().toDouble()
            val desc = binding.submitProductDescEdt.text.toString()

            // TODO o'zini pojosini qo'ysak keraksiz fieldlar ham yo'qoladi
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
