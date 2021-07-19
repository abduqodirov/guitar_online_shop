package com.abduqodirov.guitaronlineshop.view.model

data class SortingFilteringFields(
    val lowPrice: Int,
    val highPrice: Int,
    val sortBy: String, // TODO enum or sealed class. Or some dynamic stuff
    val nameFilter: String,
    val order: String
) {
    fun arePricesValid() = lowPrice < highPrice
}
