package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
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

    private lateinit var productAdapter: ProductsRecyclerAdapter

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

        initAdapter()

        observeProductsData()

        setUpViewListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeProductsData() {

        lifecycleScope.launch {
            viewModel.fetchProducts()
                .catch { e -> } // TODO catch this one also
                .collect {
                    productAdapter.submitData(it)
                }
        }
    }

    private fun initAdapter() {
        productAdapter = ProductsRecyclerAdapter(
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

        setUpViewVisibilities()
    }

    private fun setUpViewVisibilities() {
        productAdapter.addLoadStateListener { loadState ->

            binding.productsProgressBar.isVisible = loadState.source.refresh is LoadState.Loading

            binding.productsRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading

            binding.productsRetryButton.isVisible = loadState.source.refresh is LoadState.Error
            binding.productsErrorTxt.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.Error) {
                binding.productsErrorTxt.text =
                    (loadState.source.refresh as LoadState.Error).error.localizedMessage
            }

            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && productAdapter.itemCount == 0
            if (isListEmpty) {
                binding.produstsEmptyListTxt.text = getString(R.string.no_products)
                binding.produstsEmptyListTxt.isVisible = isListEmpty
            }
        }
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
            observeProductsData()
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
