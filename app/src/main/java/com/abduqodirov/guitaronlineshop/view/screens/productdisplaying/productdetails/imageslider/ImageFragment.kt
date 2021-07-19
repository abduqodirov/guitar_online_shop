package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.imageslider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.FragmentImageBinding
import com.abduqodirov.guitaronlineshop.view.util.loadImageFromNetwork

const val ARG_IMAGE = "image"

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentImageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf {
            it.containsKey(ARG_IMAGE).apply {

                binding.pagerItemImage.loadImageFromNetwork(
                    url = it.getString(ARG_IMAGE).toString(),
                    errorImg = R.drawable.img
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageFragment()
    }
}
