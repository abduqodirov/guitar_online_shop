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
import com.abduqodirov.guitaronlineshop.databinding.FragmentProductDetailsBinding
import com.abduqodirov.guitaronlineshop.network.Status
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

                            Glide.with(binding.detailsImage.context)
                                .load(product?.photos?.get(0))
                                .error(R.drawable.no_img)
                                .into(binding.detailsImage)
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