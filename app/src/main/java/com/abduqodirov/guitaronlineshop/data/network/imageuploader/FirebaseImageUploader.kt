package com.abduqodirov.guitaronlineshop.data.network.imageuploader

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resumeWithException

class FirebaseImageUploader : ImageUploader {

    @ExperimentalCoroutinesApi
    override suspend fun uploadImage(bitmap: Bitmap): String =
        suspendCancellableCoroutine { continuation ->
            val storage = FirebaseStorage.getInstance()

            val imageRef = storage.getReference("image.png")

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
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
