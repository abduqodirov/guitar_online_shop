package com.abduqodirov.guitaronlineshop.view.model

data class ProductForSendingScreen(
    val name: String,
    val price: Double,
    val description: String,
    val photos: List<String>
)
