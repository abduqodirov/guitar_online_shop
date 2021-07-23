package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.filtering

import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

class SortingAndFilteringChangeListener(
    private val listener: (sortingAndFilteringFields: SortingFilteringFields) -> Unit
) {
    fun onFieldsChangeListener(sortingAndFilteringFields: SortingFilteringFields) =
        listener(sortingAndFilteringFields)
}
