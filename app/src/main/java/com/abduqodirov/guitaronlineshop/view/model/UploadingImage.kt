package com.abduqodirov.guitaronlineshop.view.model

import android.graphics.Bitmap

data class UploadingImage(
    val id: Int,
    val thumbnailBitmap: Bitmap? = null,
    val path: String
)
