package com.abduqodirov.guitaronlineshop.view.screens.productdetails.imageslider

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagesCollectionAdapter(fragment: Fragment, private val images: List<String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = images.size

    override fun createFragment(position: Int): Fragment {

        val fragment = ImageFragment()
        fragment.arguments = Bundle().apply {
            putString(ARG_IMAGE, images[position])
        }

        return fragment
    }
}
