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
import com.abduqodirov.guitaronlineshop.view.util.defaultFilteringConfigs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
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

        initAdapter()

        observeProductsData(currentFilteringFields)

        setUpViewListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
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

        setupListenerOfSearch(searchItem)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupListenerOfSearch(searchItem: MenuItem) {
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                Timber.d("Ochildi menu")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                val clearedNameFilter = currentFilteringFields.copy(nameFilter = "")
                applyFilterAndRefreshList(clearedNameFilter)
                Timber.d("Close invoked")
                Timber.d("Yopildi menu")
                return true
            }
        })
    }

    /**
     * Requests data from ViewModel and submits to RecyclerView
     *
     * @param settings Optional query parameters
     * Default value is for fetching all the products without any filter
     *
     */
    private fun observeProductsData(settings: SortingFilteringFields) {

        lifecycleScope.launch {
            viewModel.fetchProducts(settings)
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
            binding.productsFilteringBtn.isVisible =
                loadState.source.refresh is LoadState.NotLoading

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
            observeProductsData(currentFilteringFields)
        }

        binding.productsAddNewProductBtn.setOnClickListener {
            findNavController().navigate(
                ProductsListFragmentDirections.actionProductsListFragmentToSubmitNewProductFragment()
            )
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

    private fun applyFilterAndRefreshList(settings: SortingFilteringFields) {

        // If filtering settings are the same, there is no need to refresh
        if (currentFilteringFields == settings) {
            return
        }

        currentFilteringFields = settings
        observeProductsData(currentFilteringFields)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsListFragment()
    }
}
