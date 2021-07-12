package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductsListBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import javax.inject.Inject

class ProductsListFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProductsViewModel> { providerFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as ShopApplication).appComponent.productsDisplayComponent()
            .create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshProducts()

        observeProductsData()

        setUpViewListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeProductsData() {

        viewModel.products.observe(
            viewLifecycleOwner,
            {

                it?.let { response ->
                    when (response) {

                        is Response.Success -> {
                            populateViewsWithSuccessfullyFetchedData(
                                response.data
                            )
                        }

                        is Response.Failure -> {
                            switchUIToErrorState()
                        }

                        is Response.Loading -> {
                            switchUIToLoadingState()
                        }
                    }
                }
            }
        )
    }

    private fun populateViewsWithSuccessfullyFetchedData(products: List<ProductForDisplay>) {

        switchUIToSuccessState()

        if (products.isEmpty()) {
            binding.productsMessageTxt.text =
                getString(R.string.no_products)
            binding.productsMessageTxt.visibility = View.VISIBLE
        } else {

            val productAdapter = ProductsRecyclerAdapter(
                ProductsRecyclerAdapter.ProductClickListener {
                    navigateToProductDetails(it)
                }
            )

            binding.productsRecycler.adapter = productAdapter
            binding.productsRecycler.setHasFixedSize(true)

            productAdapter.submitList(products.reversed())
        }
    }

    private fun switchUIToLoadingState() {
        binding.productsProgressBar.visibility = View.VISIBLE

        binding.productsRecycler.visibility = View.GONE
        binding.productsRetryButton.visibility = View.GONE
        binding.productsMessageTxt.visibility = View.GONE
    }

    private fun switchUIToSuccessState() {
        binding.productsRecycler.visibility = View.VISIBLE

        binding.productsProgressBar.visibility = View.GONE
        binding.productsRetryButton.visibility = View.GONE
        binding.productsMessageTxt.visibility = View.GONE
    }

    private fun switchUIToErrorState() {
        binding.productsRecycler.visibility = View.INVISIBLE
        binding.productsProgressBar.visibility = View.INVISIBLE

        binding.productsMessageTxt.text = getString(R.string.product_fetching_failure)
        binding.productsRetryButton.visibility = View.VISIBLE
        binding.productsMessageTxt.visibility = View.VISIBLE
    }

    private fun navigateToProductDetails(it: ProductForDisplay) {
        findNavController().navigate(
            ProductsListFragmentDirections.actionProductsListFragmentToProductDetailsFragment(
                it.id
            )
        )
    }

    private fun setUpViewListeners() {
        binding.productsRetryButton.setOnClickListener {
            viewModel.refreshProducts()
        }

        binding.productsAddNewProductBtn.setOnClickListener {
            findNavController().navigate(
                ProductsListFragmentDirections.actionProductsListFragmentToSubmitNewProductFragment()
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsListFragment()
    }
}
