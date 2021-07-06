package com.abduqodirov.guitaronlineshop.view.mapper

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.util.formatPrice
import com.abduqodirov.guitaronlineshop.view.util.formatRatingAverage

class ProductDisplayMapper {

    fun mapFetchedProduct(fetchingProduct: FetchingProduct): ProductForDisplay {
        return ProductForDisplay(
            id = fetchingProduct.id,
            name = fetchingProduct.name,
            price = fetchingProduct.price.formatPrice(),
            description = fetchingProduct.description,
            photos = fetchingProduct.photos,
            rating = fetchingProduct.rating.average().formatRatingAverage(),
            comments = fetchingProduct.comments
        )
    }
}
