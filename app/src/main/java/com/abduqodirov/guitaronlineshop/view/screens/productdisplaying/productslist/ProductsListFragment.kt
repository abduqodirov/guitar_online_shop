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
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.adapters.ProductsLoadStateAdapter
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.adapters.ProductsRecyclerAdapter
import com.abduqodirov.guitaronlineshop.view.util.defaultFields
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

    /**
     * Requests data from ViewModel and submits to RecyclerView
     *
     * @param fields Optional query parameters
     * Default value is for fetching all the products without any filter
     *
     */
    private fun observeProductsData(fields: SortingFilteringFields = defaultFields) {

        lifecycleScope.launch {
            viewModel.fetchProducts(fields)
                .catch { e -> } // TODO catch this one also
                .collect {
                    productAdapter.submitData(it)
                }
        }
        binding.productsRecycler.scrollToPosition(0)
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
            binding.productsFilteringBtn.isVisible = loadState.source.refresh is LoadState.NotLoading

            binding.productsRetryButton.isVisible = loadState.source.refresh is LoadState.Error
            binding.productsErrorTxt.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.Error) {
                binding.productsErrorTxt.text =
                    (loadState.source.refresh as LoadState.Error).error.localizedMessage
            }

            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && productAdapter.itemCount == 0
            binding.produstsEmptyListTxt.isVisible = isListEmpty
            if (isListEmpty) {
                binding.produstsEmptyListTxt.text = getString(R.string.no_products)
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

        val filterFragment = FilteringSortingBottomSheetFragment.newInstance(
            onSortingFilteringFieldsChangeListener = SortingAndFilteringChangeListener { fields ->
                observeProductsData(fields)
            }
        )

        binding.productsFilteringBtn.setOnClickListener {
            filterFragment.show(requireActivity().supportFragmentManager, "filter")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsListFragment()
    }
}
