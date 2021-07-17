package com.abduqodirov.guitaronlineshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.FragmentImageBinding
import com.bumptech.glide.Glide

const val ARG_IMAGE = "image"

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    // TODO onDestroyda null qivorish kk

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

                Glide.with(binding.pagerItemImage.context)
                    .load(it.getString(ARG_IMAGE))
                    .error(R.drawable.no_img)

                    .into(binding.pagerItemImage)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageFragment()
    }
}
