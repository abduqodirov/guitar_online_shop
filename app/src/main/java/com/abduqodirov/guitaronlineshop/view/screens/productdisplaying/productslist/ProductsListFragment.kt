package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductsListBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.recycleradapters.ProductsLoadStateAdapter
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.recycleradapters.ProductsRecyclerAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsListFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProductsViewModel> { providerFactory }

    private var productsListScope: Job? = null

    private var counter = 0

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

        // viewModel.refreshProducts()

        observeProductsData()

        setUpViewListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeProductsData() {

        val productAdapter = ProductsRecyclerAdapter(
            ProductsRecyclerAdapter.ProductClickListener {
                navigateToProductDetails(it)
            }
        )

        binding.productsRecycler.adapter = productAdapter.withLoadStateHeaderAndFooter(
            header = ProductsLoadStateAdapter(
                retry = {
                    Log.d("fragmentda", "observeProductsData: header retyry")
                    productAdapter.retry()
                }
            ),
            footer = ProductsLoadStateAdapter(
                retry = {
                    Log.d("fragmentda", "observeProductsData: footer retyry")
                    productAdapter.retry()
                }
            )
        )
        // binding.productsRecycler.setHasFixedSize(true)

        // TODO O'zi kerakmi shu, menda bir martta chaqiriladiku
        // productsListScope?.cancel()
        lifecycleScope.launch {
            viewModel.fetchProducts()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    counter++
                    productAdapter.submitData(it)
                    switchUIToSuccessState()
                }
        }

        // viewModel.products.observe(
        //     viewLifecycleOwner,
        //     {
        //
        //         it?.let { response ->
        //             when (response) {
        //
        //                 is Response.Success -> {
        //                     populateViewsWithSuccessfullyFetchedData(
        //                         response.data
        //                     )
        //                 }
        //
        //                 is Response.Failure -> {
        //                     switchUIToErrorState()
        //                 }
        //
        //                 is Response.Loading -> {
        //                     switchUIToLoadingState()
        //                 }
        //             }
        //         }
        //     }
        // )
    }

    // TODO pagingga maxSize berish kerak, bo'lmasa juda ko'p productlarni saqlavoradi.

    // private fun populateViewsWithSuccessfullyFetchedData(products: List<ProductForDisplay>) {
    //
    //     switchUIToSuccessState()
    //
    //     if (products.isEmpty()) {
    //         binding.productsMessageTxt.text =
    //             getString(R.string.no_products)
    //         binding.productsMessageTxt.visibility = View.VISIBLE
    //     } else {
    //
    //         val productAdapter = ProductsRecyclerAdapter(
    //             ProductsRecyclerAdapter.ProductClickListener {
    //                 navigateToProductDetails(it)
    //             }
    //         )
    //
    //         binding.productsRecycler.adapter = productAdapter
    //         binding.productsRecycler.setHasFixedSize(true)
    //
    //     }
    // }

    // TODO kerakmikin mengayam shu
    // private fun initSearch(query: String) {
    //     // First part of the method is unchanged
    //
    //     // Scroll to top when the list is refreshed from network.
    //     lifecycleScope.launch {
    //         adapter.loadStateFlow
    //             // Only emit when REFRESH LoadState changes.
    //             .distinctUntilChangedBy { it.refresh }
    //             // Only react to cases where REFRESH completes i.e., NotLoading.
    //             .filter { it.refresh is LoadState.NotLoading }
    //             .collect { binding.list.scrollToPosition(0) }
    //     }
    // }

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
            // viewModel.refreshProducts()
            viewModel.fetchProducts()
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
