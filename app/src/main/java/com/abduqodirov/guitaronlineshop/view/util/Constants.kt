package com.abduqodirov.guitaronlineshop.view.util

import com.abduqodirov.guitaronlineshop.view.model.SortOrderOption
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

// API returns all the products if request is filtered with 0 (zero) price.
const val OMIT_PRICE_FILTER_VALUE = 0

val orders = arrayOf("ASC", "DESC")

val sortByOptions = arrayOf("name", "price")

val sortOrderOptions = arrayOf(
    SortOrderOption(
        "Price: low to high",
        sortByOptions[1],
        orders[0]
    ),
    SortOrderOption(
        "Price: high to low",
        sortByOptions[1],
        orders[1]
    )
)

val defaultFields =
    SortingFilteringFields(
        OMIT_PRICE_FILTER_VALUE,
        OMIT_PRICE_FILTER_VALUE,
        sortByOptions[0],
        "",
        orders[0]
    )
