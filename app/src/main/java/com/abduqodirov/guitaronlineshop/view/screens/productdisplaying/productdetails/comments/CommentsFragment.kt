package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.comments

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
import androidx.navigation.fragment.navArgs
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.databinding.FragmentCommentsBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.mapper.mapFetchedProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.ProductDetailsFragmentArgs
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.ProductDetailsViewModel
import javax.inject.Inject

class CommentsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<ProductDetailsViewModel>() { viewModelFactory }

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentAdapter: CommentsRecyclerAdapter

    private val args: ProductDetailsFragmentArgs by navArgs()

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
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()

        observeComments()
    }

    private fun observeComments() {
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
                            displayProductComments(
                                mapFetchedProduct(response.data)
                            )
                        }

                        is Response.Failure -> {
                            binding.commentsMessageTxt.text = response.errorMessage
                            switchUItoErrorState()
                        }
                    }
                }
            }
        )
    }

    private fun displayProductComments(product: ProductForDisplay) {
        commentAdapter.submitList(product.comments)
    }

    private fun setupAdapters() {
        commentAdapter = CommentsRecyclerAdapter()

        binding.run {
            commentsRecycler.setHasFixedSize(true)
            commentsRecycler.adapter = commentAdapter
        }
    }

    private fun switchUItoSuccessState() {
        binding.run {
            commentsProgress.isVisible = false
            commentsErrorGroup.isVisible = false

            commentsRecycler.isVisible = true
        }
    }

    private fun switchUItoErrorState() {
        binding.run {
            commentsErrorGroup.isVisible = true

            commentsProgress.isVisible = false
            commentsRecycler.isVisible = false
        }
    }

    private fun switchUItoLoadingState() {
        binding.run {
            commentsProgress.isVisible = true

            commentsRecycler.isVisible = false
            commentsErrorGroup.isVisible = false
        }
    }
}
