package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductDetailsBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.mapper.mapFetchedProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.imageslider.ImagesCollectionAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class ProductDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<ProductDetailsViewModel> { viewModelFactory }

    private lateinit var imagesCollectionAdapter: ImagesCollectionAdapter
    private lateinit var commentAdapter: CommentsRecyclerAdapter

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

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    // TODO If comments array is empty or null, animation should be removed.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.productId

        viewModel.refreshProduct(id)

        observeProductDetails()

        binding.detailsRetryBtn.setOnClickListener {
            viewModel.refreshProduct(id)
        }

        setupAdapters()
        setUpTabLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeProductDetails() {
        viewModel.product.observe(
            viewLifecycleOwner,
            Observer {

                it.let { response ->

                    when (response) {

                        is Response.Loading -> {
                            switchUItoLoadingState()
                        }

                        is Response.Success -> {
                            switchUItoSuccessState()
                            populateViewsWithSuccessfullyFetchedData(
                                mapFetchedProduct(response.data)
                            )
                        }

                        is Response.Failure -> {
                            switchUItoErrorState()
                            binding.detailsMessageTxt.text = response.errorMessage
                        }
                    }
                }
            }
        )
    }

    private fun populateViewsWithSuccessfullyFetchedData(product: ProductForDisplay) {

        binding.run {
            detailsNameTxt.text = product.name
            detailsPriceTxt.text = product.price
            detailsDescTxt.text = product.description

            detailsRatingGroup.isVisible = product.rating.isEmpty()
            detailsRatingTxt.text = product.rating
        }

        commentAdapter.submitList(product.comments)
        imagesCollectionAdapter.submitList(product.photos)
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(
            binding.detailsImageTabLayout,
            binding.detailsImagePager
        ) { _, _ ->
        }.attach()
    }

    private fun setupAdapters() {
        // TODO Invalid image urls should be removed. Because Viewpager is showing several no_img illustrations.
        // Maybe we could count failed images, and restrict ViewPager to include only 1 failed image.
        imagesCollectionAdapter = ImagesCollectionAdapter(this)
        commentAdapter = CommentsRecyclerAdapter()

        binding.run {
            detailsImagePager.adapter = imagesCollectionAdapter

            detailsCommentsRecycler.setHasFixedSize(true)
            detailsCommentsRecycler.adapter = commentAdapter
        }
    }

    private fun switchUItoSuccessState() {
        binding.run {
            detailsProgressBar.visibility = View.INVISIBLE
            detailsErrorGroup.visibility = View.INVISIBLE

            detailsDataGroup.visibility = View.VISIBLE
        }
    }

    private fun switchUItoErrorState() {
        binding.run {
            detailsErrorGroup.visibility = View.VISIBLE

            detailsProgressBar.visibility = View.INVISIBLE
            detailsDataGroup.visibility = View.INVISIBLE
        }
    }

    private fun switchUItoLoadingState() {
        binding.run {
            detailsProgressBar.visibility = View.VISIBLE

            detailsDataGroup.visibility = View.INVISIBLE
            detailsErrorGroup.visibility = View.INVISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductDetailsFragment()
    }
}
