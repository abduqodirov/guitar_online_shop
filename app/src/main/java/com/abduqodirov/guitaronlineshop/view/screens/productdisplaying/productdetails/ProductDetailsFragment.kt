package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductDetailsBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.mapper.mapFetchedProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.comments.CommentsRecyclerAdapter
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.imageslider.ImagesCollectionAdapter
import com.abduqodirov.guitaronlineshop.view.util.COMMENTS_LIMIT_IN_PRODUCT_DETAILS
import com.abduqodirov.guitaronlineshop.view.util.DESCRIPTION_MAX_LINES_COLLAPSED
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class ProductDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<ProductDetailsViewModel> { viewModelFactory }

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var productId: String

    private lateinit var imagesCollectionAdapter: ImagesCollectionAdapter
    private lateinit var commentsAdapter: CommentsRecyclerAdapter

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

        productId = args.productId

        viewModel.refreshProduct(productId)

        observeProductDetails()

        setupClickListeners()

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

            imagesCollectionAdapter.submitList(product.photos)

            var commentsLimit = COMMENTS_LIMIT_IN_PRODUCT_DETAILS
            if (product.comments.size < commentsLimit) {
                commentsLimit = product.comments.size
                detailsCommentsBtn.isVisible = false
            } else {
                detailsCommentsBtn.isVisible = true
            }

            commentsAdapter.submitList(product.comments.subList(0, commentsLimit))
        }
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

        commentsAdapter = CommentsRecyclerAdapter()
        commentsAdapter.collapseComments = true

        binding.run {
            detailsImagePager.adapter = imagesCollectionAdapter

            detailsCommentsRecycler.setHasFixedSize(true)
            detailsCommentsRecycler.adapter = commentsAdapter

            val layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically() = false
            }
            detailsCommentsRecycler.layoutManager = layoutManager
        }
    }

    private fun setupClickListeners() {
        binding.run {
            detailsRetryBtn.setOnClickListener {
                viewModel.refreshProduct(productId)
            }

            detailsCommentsBtn.setOnClickListener {
                navigateToCommentsScreen()
            }

            detailsCommentsRecycler.setOnClickListener {
                navigateToCommentsScreen()
            }

            detailsDescExpandCollapseBtn.setOnClickListener {
                if (detailsDescTxt.maxLines == Int.MAX_VALUE) {
                    detailsDescTxt.maxLines = DESCRIPTION_MAX_LINES_COLLAPSED
                } else {
                    detailsDescTxt.maxLines = Int.MAX_VALUE
                    detailsScrollView.post {
                        detailsScrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
        }
    }

    private fun switchUItoSuccessState() {
        binding.run {
            detailsProgressBar.hide()
            detailsErrorGroup.isVisible = false

            detailsDataGroup.isVisible = true
        }
    }

    private fun switchUItoErrorState() {
        binding.run {
            detailsErrorGroup.isVisible = true

            detailsProgressBar.hide()
            detailsDataGroup.isVisible = false
        }
    }

    private fun switchUItoLoadingState() {
        binding.run {
            detailsProgressBar.show()

            detailsDataGroup.isVisible = false
            detailsErrorGroup.isVisible = false
        }
    }

    private fun navigateToCommentsScreen() {
        findNavController().navigate(
            ProductDetailsFragmentDirections.actionProductDetailsFragmentToCommentsFragment()
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductDetailsFragment()
    }
}
