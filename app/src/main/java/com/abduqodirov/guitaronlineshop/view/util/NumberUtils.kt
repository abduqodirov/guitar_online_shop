package com.abduqodirov.guitaronlineshop.view.util

import java.text.DecimalFormat

fun Double.formatPrice(): String {
    val formatter = DecimalFormat("0.##")

    return formatter.format(this)
}

fun Double.formatRatingAverage(): String {
    val formatter = DecimalFormat("0.#")

    return formatter.format(this)
}
