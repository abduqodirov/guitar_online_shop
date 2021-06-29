package com.abduqodirov.guitaronlineshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.adapter.CommentsRecyclerAdapter
import com.abduqodirov.guitaronlineshop.adapter.ImagesCollectionAdapter
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductDetailsBinding
import com.abduqodirov.guitaronlineshop.network.Status.*
import com.abduqodirov.guitaronlineshop.viewmodel.ProductDetailsViewModel
import com.bumptech.glide.Glide

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.productId

        val viewModel: ProductDetailsViewModel by viewModels()

        viewModel.refreshProduct(id)

        viewModel.productLive.observe(viewLifecycleOwner, Observer {

            it.let { response ->

                when (response.status) {

                    LOADING -> {
                        binding.detailsDataGroup.visibility = View.INVISIBLE
                        binding.detailsErrorGroup.visibility = View.INVISIBLE
                        binding.detailsProgressBar.visibility = View.VISIBLE

                    }

                    SUCCESS -> {

                        binding.detailsProgressBar.visibility = View.INVISIBLE
                        binding.detailsErrorGroup.visibility = View.INVISIBLE
                        binding.detailsDataGroup.visibility = View.VISIBLE
                        response.data.let { product ->

                            binding.detailsNameTxt.text = product?.name
                            binding.detailsPriceTxt.text = product?.price.toString()
                            binding.detailsDescTxt.text = product?.description

                            binding.detailsRatingTxt.text = product?.rating?.average().toString()

                            if (product?.rating?.isEmpty() == true) {
                                binding.detailsRatingGroup.visibility = View.INVISIBLE
                            }


                            //TODO shu va boshqa screenlarni datasini qo'yib chiqishni successni
                            // ichida yozmasdan alohida methodga olib chiqish kerak
                            // shunda let bilan tekshirish, ? lar ham yo'qoladi.

                            val imagesCollectionAdapter =
                                product?.let { it1 ->
                                    ImagesCollectionAdapter(
                                        this,
                                        it1.photos
                                    )
                                }
                            binding.detailsImagePager.adapter = imagesCollectionAdapter


                            //TODO empty comments shouldn't be displayed
                            val commentAdapter = CommentsRecyclerAdapter()

                            binding.detailsCommentsRecycler.setHasFixedSize(true)
                            binding.detailsCommentsRecycler.adapter = commentAdapter
                            commentAdapter.submitList(product?.comments)
                        }


                    }

                    ERROR -> {
                        binding.detailsProgressBar.visibility = View.INVISIBLE
                        binding.detailsDataGroup.visibility = View.INVISIBLE
                        binding.detailsErrorGroup.visibility = View.VISIBLE
                        binding.detailsMessageTxt.text = it.message
                    }

                }
            }

        })

        binding.detailsRetryBtn.setOnClickListener {
            viewModel.refreshProduct(id)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductDetailsFragment()
    }
}