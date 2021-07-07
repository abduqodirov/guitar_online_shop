package com.abduqodirov.guitaronlineshop.data.network.imageuploader

import android.graphics.Bitmap

interface ImageUploader {

    suspend fun uploadImage(bitmap: Bitmap): String
}
