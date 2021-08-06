package com.abduqodirov.guitaronlineshop.data.mapper

import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImagesDTO
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen

fun mapSubmittingProduct(
    productForSending: ProductForSendingScreen,
    images: List<String>
): SendingProductWithUploadedImagesDTO {
    // TODO: 8/6/2021 temporary mock amount
    return SendingProductWithUploadedImagesDTO(
        name = productForSending.name,
        price = productForSending.price,
        description = productForSending.description,
        photos = images,
        comments = listOf(),
        rating = listOf(),
        amount = 120
    )
}
