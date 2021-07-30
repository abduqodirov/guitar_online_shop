package com.abduqodirov.guitaronlineshop.view.model

data class Validation(
    val editTextPosition: Int,
    var errorResId: Int? = null,
    val validator: (text: String) -> Int?
)
