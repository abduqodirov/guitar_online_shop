package com.abduqodirov.guitaronlineshop.data.model

sealed class Response<out T : Any> {

    data class Success<out T : Any>(val data: T) : Response<T>()

    data class Failure(val errorMessage: String?) : Response<Nothing>()

    object Loading : Response<Nothing>()
}
