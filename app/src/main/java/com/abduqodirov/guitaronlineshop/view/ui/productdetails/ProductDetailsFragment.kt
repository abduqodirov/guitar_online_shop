package com.abduqodirov.guitaronlineshop.view.ui.productdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.network.Status.ERROR
import com.abduqodirov.guitaronlineshop.data.network.Status.LOADING
import com.abduqodirov.guitaronlineshop.data.network.Status.SUCCESS
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductDetailsBinding
import com.abduqodirov.guitaronlineshop.view.ui.ShopApplication
import com.abduqodirov.guitaronlineshop.view.ui.productdetails.imageslider.ImagesCollectionAdapter
import com.abduqodirov.guitaronlineshop.view.util.formatPrice
import com.abduqodirov.guitaronlineshop.view.util.formatRatingAverage
import javax.inject.Inject

class ProductDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<ProductDetailsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as ShopApplication).appComponent.inject(this)
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

                    when (response.status) {

                        LOADING -> {
                            switchUItoLoadingState()
                        }

                        SUCCESS -> {
                            response.data.let { product ->

                                product?.let { nullSafeProduct ->
                                    populateViewsWithSuccessfullyFetchedData(nullSafeProduct as FetchingProduct)
                                }
                            }
                        }

                        ERROR -> {
                            switchUItoErrorState()
                            binding.detailsMessageTxt.text = it.message
                        }
                    }
                }
            }
        )
    }

    private fun populateViewsWithSuccessfullyFetchedData(product: FetchingProduct) {
        binding.detailsProgressBar.visibility = View.INVISIBLE
        binding.detailsErrorGroup.visibility = View.INVISIBLE
        binding.detailsDataGroup.visibility = View.VISIBLE

        binding.detailsNameTxt.text = product.name
        binding.detailsPriceTxt.text = product.price.formatPrice()
        binding.detailsDescTxt.text = product.description

        binding.detailsRatingTxt.text =
            product.rating.average().formatRatingAverage()

        if (product.rating.isEmpty()) {
            binding.detailsRatingGroup.visibility = View.INVISIBLE
        }

        val imagesCollectionAdapter =
            ImagesCollectionAdapter(
                this,
                product.photos
            )
        binding.detailsImagePager.adapter = imagesCollectionAdapter

        // TODO empty comments shouldn't be displayed
        val commentAdapter = CommentsRecyclerAdapter()

        binding.detailsCommentsRecycler.setHasFixedSize(true)
        binding.detailsCommentsRecycler.adapter = commentAdapter
        commentAdapter.submitList(product.comments)
    }

    private fun switchUItoErrorState() {
        binding.detailsErrorGroup.visibility = View.VISIBLE

        binding.detailsProgressBar.visibility = View.INVISIBLE
        binding.detailsDataGroup.visibility = View.INVISIBLE
    }

    private fun switchUItoLoadingState() {
        binding.detailsProgressBar.visibility = View.VISIBLE

        binding.detailsDataGroup.visibility = View.INVISIBLE
        binding.detailsErrorGroup.visibility = View.INVISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductDetailsFragment()
    }
}
