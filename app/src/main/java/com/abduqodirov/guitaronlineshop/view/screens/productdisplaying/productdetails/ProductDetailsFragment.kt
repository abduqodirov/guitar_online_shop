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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.productId

        viewModel.refreshProduct(id)

        observeProductDetails()

        binding.detailsRetryBtn.setOnClickListener {
            viewModel.refreshProduct(id)
        }

        setupAdapters()
        setupTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
                            binding.detailsMessageTxt.text = response.errorMessage
                            switchUItoErrorState()
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

            detailsRatingGroup.isVisible = product.rating.isNotEmpty()
            detailsRatingTxt.text = product.rating
        }

        commentAdapter.submitList(product.comments)
        imagesCollectionAdapter.submitList(product.photos)
    }

    private fun setupTabLayout() {
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
            detailsProgressBar.isVisible = false
            detailsErrorGroup.isVisible = false

            detailsDataGroup.isVisible = true
        }
    }

    private fun switchUItoErrorState() {
        binding.run {
            detailsErrorGroup.isVisible = true

            detailsProgressBar.isVisible = false
            detailsDataGroup.isVisible = false
        }
    }

    private fun switchUItoLoadingState() {
        binding.run {
            detailsProgressBar.isVisible = true

            detailsDataGroup.isVisible = false
            detailsErrorGroup.isVisible = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductDetailsFragment()
    }
}
