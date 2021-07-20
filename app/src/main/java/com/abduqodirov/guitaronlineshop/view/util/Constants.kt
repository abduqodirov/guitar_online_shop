package com.abduqodirov.guitaronlineshop.view.util

import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.view.model.SortOrderOption
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

// API returns all the products if request is filtered with 0 (zero) price.
const val OMIT_PRICE_FILTER_VALUE = 0

const val OMIT_NAME_FILTER = ""

const val ORDER_ASC = "ASC"
const val ORDER_DESC = "DESC"

const val ORDER_BY_NAME = "name"
const val ORDER_BY_PRICE = "price"

val sortOrderOptions = arrayOf(
    SortOrderOption(
        R.string.sort_price_min_to_max,
        ORDER_BY_PRICE,
        ORDER_ASC
    ),
    SortOrderOption(
        R.string.sort_price_max_to_min,
        ORDER_BY_PRICE,
        ORDER_DESC
    )
)

val defaultFilteringConfigs =
    SortingFilteringFields(
        OMIT_PRICE_FILTER_VALUE,
        OMIT_PRICE_FILTER_VALUE,
        ORDER_BY_PRICE,
        OMIT_NAME_FILTER,
        ORDER_ASC
    )
