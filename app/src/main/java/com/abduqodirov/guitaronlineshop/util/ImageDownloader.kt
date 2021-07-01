package com.abduqodirov.guitaronlineshop.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageFromNetwork(url: String, errorImg: Int) {

    Glide.with(this.context)
        .load(url)
        .error(errorImg)
        .into(this)
}
