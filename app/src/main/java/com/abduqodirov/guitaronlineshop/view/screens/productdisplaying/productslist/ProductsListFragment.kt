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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PRODUCTS_FRAGMENT_RESULT_KEY = "produtcs_fragment_result"

const val PRODUCTS_FRAGMENT_LOW_PRICE = "products_low_price"

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

        observeProductsData()

        setUpViewListeners()

        // setupFilteringListener()
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
                    productAdapter.retry()
                }
            ),
            footer = ProductsLoadStateAdapter(
                retry = {
                    productAdapter.retry()
                }
            )
        )
        binding.productsRecycler.setHasFixedSize(true)

        lifecycleScope.launch {
            viewModel.fetchProducts()
                .catch { e ->
                    Log.d("ftt", "observeProductsData: fragmentda tutdim")
                    e.printStackTrace()
                }
                .collect {
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
            viewModel.fetchProducts()
        }

        binding.productsAddNewProductBtn.setOnClickListener {
            findNavController().navigate(
                ProductsListFragmentDirections.actionProductsListFragmentToSubmitNewProductFragment()
            )
        }

        val filterFragment = FilteringSortingBottomSheetFragment()

        binding.productsFilteringBtn.setOnClickListener {
            filterFragment.show(requireActivity().supportFragmentManager, "filter")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsListFragment()
    }
}
