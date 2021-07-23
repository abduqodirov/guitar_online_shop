package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.imagechooser

class ImageSourceCallback(
    private val listener: (source: ImageSource) -> Unit
) {
    fun onSourceSelected(source: ImageSource) = listener(source)
}
