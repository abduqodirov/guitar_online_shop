package com.abduqodirov.guitaronlineshop.view.mapper

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.util.formatPrice
import com.abduqodirov.guitaronlineshop.view.util.formatRatingAverage

fun mapFetchedProduct(fetchingProduct: FetchingProduct): ProductForDisplay {

    var formattedRatingAverage = ""
    var ratingAverage = 0.0

    fetchingProduct.rating.forEach {
        if (it != null) {
            ratingAverage += it
        }
    }

    ratingAverage /= fetchingProduct.rating.size
    if (ratingAverage > 1.0) {
        formattedRatingAverage = ratingAverage.formatRatingAverage()
    }

    // TODO we will remove once we restricted null and empty string values on backend
    val validatedComments = arrayListOf<String>()

    fetchingProduct.comments.forEach {
        if (it != null && it.isNotEmpty()) {
            validatedComments.add(it)
        }
    }

    val validatedPhotos = arrayListOf<String>()
    fetchingProduct.photos.forEach {
        if (it != null && it.isNotEmpty()) {
            validatedPhotos.add(it)
        }
    }
    if (validatedPhotos.isEmpty()) {
        validatedPhotos.add("")
    }

    return ProductForDisplay(
        id = fetchingProduct.id,
        name = fetchingProduct.name,
        price = fetchingProduct.price.formatPrice(),
        description = fetchingProduct.description,
        photos = validatedPhotos,
        rating = formattedRatingAverage,
        comments = validatedComments
    )
}
