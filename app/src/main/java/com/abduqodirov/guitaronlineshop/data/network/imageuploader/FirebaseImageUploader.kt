package com.abduqodirov.guitaronlineshop.data.network.imageuploader

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resumeWithException

class FirebaseImageUploader : ImageUploader {

    @ExperimentalCoroutinesApi
    override suspend fun uploadImage(bitmap: Bitmap, name: String): String =
        suspendCancellableCoroutine { continuation ->
            val storage = FirebaseStorage.getInstance()

            // TODO hardcoded image filename. Impact: overwrites images.
            val imageRef = storage.getReference("${name}_${System.currentTimeMillis()}.jpg")

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos)
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
