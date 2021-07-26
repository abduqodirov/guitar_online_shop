package com.abduqodirov.guitaronlineshop.data.network.imageuploader

import android.graphics.Bitmap
import com.abduqodirov.guitaronlineshop.data.network.IMAGE_UPLOADING_TIMEOUT
import com.abduqodirov.guitaronlineshop.data.network.SENDING_IMAGE_QUALITY
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resumeWithException

class FirebaseImageUploader : ImageUploader {

    @ExperimentalCoroutinesApi
    override suspend fun uploadImage(bitmap: Bitmap, name: String): String? {

        return withTimeoutOrNull(IMAGE_UPLOADING_TIMEOUT) {
            return@withTimeoutOrNull suspendCancellableCoroutine { continuation ->
                val storage = FirebaseStorage.getInstance()

                val imageRef = storage.getReference("${name}_${System.currentTimeMillis()}.jpg")

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, SENDING_IMAGE_QUALITY, baos)
                val uploadingData = baos.toByteArray()

                val uploadTask = imageRef.putBytes(uploadingData)

                uploadTask.addOnCompleteListener {
                    imageRef.downloadUrl.addOnCompleteListener { url ->
                        val toString = url.result.toString()
                        continuation.resume(toString) {}
                    }
                }

                uploadTask.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            }
        }
    }
}
