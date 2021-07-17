package com.abduqodirov.guitaronlineshop.view.util

import java.text.DecimalFormat

fun Double.formatPrice(): String {
    val formatter = DecimalFormat("0.##")

    return formatter.format(this)
}

fun Double.formatRatingAverage(): String {
    val formatter = DecimalFormat("0.#")

    // TODO NaN ratingni ko'rsatmasdan o'rniga ratingni hide qilib qo'yavergan yaxshi
    // TODO 0.0 rating bo'lsa ham ko'rsatmaslik kerak
    return formatter.format(this)
}

fun String.toIntOrZeroIfEmpty(): Int {
    return if (this.isEmpty()) {
        0
    } else {
        this.toInt()
    }
}
