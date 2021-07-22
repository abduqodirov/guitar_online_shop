package com.abduqodirov.guitaronlineshop.view.util

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

fun ImageView.loadImageFromNetwork(url: String, errorImg: Int) {

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide.with(this.context)
        .load(url)
        .placeholder(circularProgressDrawable)
        .error(errorImg)
        .into(this)
}
