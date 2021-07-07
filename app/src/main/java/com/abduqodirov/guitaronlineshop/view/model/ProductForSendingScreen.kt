package com.abduqodirov.guitaronlineshop.view.model

import android.graphics.Bitmap

data class ProductForSendingScreen(
    val name: String,
    val price: Double,
    val description: String,
    val photos: List<Bitmap>,
)
