package com.abduqodirov.guitaronlineshop.data.mapper

import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImages
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen

fun mapSubmittingProduct(
    productForSending: ProductForSendingScreen,
    images: List<String>
): SendingProductWithUploadedImages {
    return SendingProductWithUploadedImages(
        name = productForSending.name,
        price = productForSending.price,
        description = productForSending.description,
        photos = images,
        comments = listOf(""),
        rating = listOf(),
    )
}
