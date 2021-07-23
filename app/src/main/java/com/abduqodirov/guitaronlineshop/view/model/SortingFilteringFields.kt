package com.abduqodirov.guitaronlineshop.view.model

data class SortingFilteringFields(
    val lowPrice: Int,
    val highPrice: Int,
    val sortBy: String,
    val nameFilter: String,
    val order: String
) {
    fun arePricesValid(): Boolean {
        return if (highPrice == 0 && lowPrice == 0) {
            true
        } else {
            lowPrice < highPrice
        }
    }
}
