package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.imageslider

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagesCollectionAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private var data: List<String> = listOf()

    fun submitList(images: List<String>) {
        data = images
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun createFragment(position: Int): Fragment {

        val fragment = ImageFragment.newInstance()
        fragment.arguments = Bundle().apply {
            putString(ARG_IMAGE, data[position])
        }

        return fragment
    }
}
