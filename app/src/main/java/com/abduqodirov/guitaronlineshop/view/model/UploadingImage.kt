package com.abduqodirov.guitaronlineshop.view.model

import android.graphics.Bitmap

data class UploadingImage(
    val id: Int,
    val bitmap: Bitmap? = null,
    val path: String? = null
)
