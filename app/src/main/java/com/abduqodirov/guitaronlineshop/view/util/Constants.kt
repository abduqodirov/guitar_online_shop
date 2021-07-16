package com.abduqodirov.guitaronlineshop.view.util

import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

// API returns all the products if request is filtered with 0 (zero) price.
const val OMIT_PRICE_FILTER_VALUE = 0

val sortByOptions = arrayOf("name", "price") // TODO make dynamic

val orders = arrayOf("ASC", "DESC")

val defaultFields =
    SortingFilteringFields(
        OMIT_PRICE_FILTER_VALUE,
        OMIT_PRICE_FILTER_VALUE,
        sortByOptions[0],
        "",
        orders[0]
    )
