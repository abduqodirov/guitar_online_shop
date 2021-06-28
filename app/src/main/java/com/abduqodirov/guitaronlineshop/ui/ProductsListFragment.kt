package com.abduqodirov.guitaronlineshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.adapter.ProductsRecyclerAdapter
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductsListBinding
import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.model.Product
import com.abduqodirov.guitaronlineshop.network.Status.*
import com.abduqodirov.guitaronlineshop.viewmodel.ProductsViewModel

class ProductsListFragment : Fragment() {

    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productAdapter = ProductsRecyclerAdapter(ProductsRecyclerAdapter.ProductClickListener {

            navigateToProductDetails(it)

        })

        binding.productsRecycler.adapter = productAdapter
        binding.productsRecycler.setHasFixedSize(true)

        val viewModel: ProductsViewModel by viewModels()

        viewModel.refreshProducts()

        viewModel.products.observe(viewLifecycleOwner, {

            it?.let { response ->
                when (response.status) {

                    SUCCESS -> {
                        stopProgressBar()
                        response.data.let { products ->
                            if (products == null || products.isEmpty()) {
                                binding.productsMessageTxt.text = getString(R.string.no_products)
                                binding.productsMessageTxt.visibility = View.VISIBLE
                            } else {
                                productAdapter.submitList(products.reversed())
                            }

                        }
                    }

                    ERROR -> {
                        stopProgressBar()
                        binding.productsMessageTxt.text =
                            getString(R.string.product_fetching_failure)
                        binding.productsRetryButton.visibility = View.VISIBLE
                        binding.productsMessageTxt.visibility = View.VISIBLE
                        binding.productsRecycler.visibility = View.INVISIBLE
                    }

                    LOADING -> {
                        startProgressBar()
                    }

                }
            }

        })

        binding.productsRetryButton.setOnClickListener {
            viewModel.refreshProducts()
        }

        binding.productsAddNewProductBtn.setOnClickListener {
            findNavController().navigate(
                ProductsListFragmentDirections.actionProductsListFragmentToSubmitNewProductFragment()
            )
        }

    }

    private fun navigateToProductDetails(it: Product) {
        findNavController().navigate(
            ProductsListFragmentDirections.actionProductsListFragmentToProductDetailsFragment((it as FetchingProduct).id)

        )
    }

    private fun startProgressBar() {
        binding.productsProgressBar.visibility = View.VISIBLE
        binding.productsRecycler.visibility = View.GONE
        binding.productsRetryButton.visibility = View.GONE
        binding.productsMessageTxt.visibility = View.GONE

    }

    private fun stopProgressBar() {
        binding.productsProgressBar.visibility = View.GONE
        binding.productsRecycler.visibility = View.VISIBLE
        binding.productsRetryButton.visibility = View.GONE

    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsListFragment()
    }
}