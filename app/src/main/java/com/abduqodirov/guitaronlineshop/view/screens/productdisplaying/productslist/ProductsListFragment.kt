package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.filtering.FilteringSortingBottomSheetFragment
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.filtering.SortingAndFilteringChangeListener
import com.abduqodirov.guitaronlineshop.view.util.defaultFilteringConfigs
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

    private var currentFilteringFields: SortingFilteringFields = defaultFilteringConfigs

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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        observeProductsData(currentFilteringFields)

        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.products_list_search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.isIconified = false

        setUpSearchView(searchView)

        setupSearchListener(searchItem)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun observeProductsData(settings: SortingFilteringFields) {

        lifecycleScope.launch {
            viewModel.fetchProducts(settings)
                .collect {
                    productAdapter.submitData(it)
                }
        }
        setUpViewVisibilities()
        binding.productsRecycler.scrollToPosition(0)
    }

    private fun applyFilterAndRefreshList(settings: SortingFilteringFields) {
        // If filtering settings are the same, there is no need to refresh
        if (currentFilteringFields == settings) {
            return
        }
        currentFilteringFields = settings
        observeProductsData(currentFilteringFields)
    }

    private fun setupSearchListener(searchItem: MenuItem) {
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                val clearedNameFilter = currentFilteringFields.copy(nameFilter = "")
                applyFilterAndRefreshList(clearedNameFilter)
                return true
            }
        })
    }

    private fun setupAdapter() {
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
    }

    private fun setUpViewVisibilities() {
        // TODO Binding is already null until Paging returned a response.
        // TODO Fragment shouldn't listen anymore once its view destroyed. Or Coroutine and flow shouldn't give data
        // TODO Fragment instance should be kept when switched to another tab

        productAdapter.addLoadStateListener { loadState ->

            binding.run {

                productsProgressBar.isVisible = loadState.source.refresh is LoadState.Loading

                productsRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                productsFilteringBtn.isVisible = loadState.source.refresh is LoadState.NotLoading

                productsRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                productsErrorTxt.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.Error) {
                    productsErrorTxt.text =
                        (loadState.source.refresh as LoadState.Error).error.localizedMessage ?: getString(R.string.product_fetching_failure)
                }

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && productAdapter.itemCount == 0
                productsEmptyListTxt.isVisible = isListEmpty
                if (isListEmpty) {
                    productsEmptyListTxt.text = getString(R.string.no_products)
                }
            }
        }
    }

    private fun setupClickListeners() {

        binding.productsRetryButton.setOnClickListener {
            observeProductsData(currentFilteringFields)
        }

        val filterFragment = FilteringSortingBottomSheetFragment.newInstance(
            onSortingFilteringFieldsChangeListener = SortingAndFilteringChangeListener { fields ->

                // Persevering nameFilter between filtering changes
                applyFilterAndRefreshList(
                    fields.copy(
                        nameFilter = currentFilteringFields.nameFilter
                    )
                )
            }
        )

        binding.productsFilteringBtn.setOnClickListener {
            filterFragment.show(requireActivity().supportFragmentManager, "filter")
        }
    }

    private fun setUpSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filterSettingsWithSearch = currentFilteringFields.copy(
                    nameFilter = query ?: ""
                )

                applyFilterAndRefreshList(filterSettingsWithSearch)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun navigateToProductDetails(it: ProductForDisplay) {
        findNavController().navigate(
            ProductsListFragmentDirections.actionProductsListFragmentToProductDetailsFragment(it.id)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsListFragment()
    }
}
