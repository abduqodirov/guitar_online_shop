package com.abduqodirov.guitaronlineshop.view.model

data class ProductForDisplay(
    val id: String,
    val name: String,
    val price: String,
    val description: String,
    val photos: List<String>,
    val rating: String,
    val comments: List<String>
)
