package com.abduqodirov.guitaronlineshop.view.util

import com.abduqodirov.guitaronlineshop.view.model.Validation
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setErrorTextResId(validation: Validation) {
    val errorRes = validation.errorResId
    error = if (errorRes != null) {
        context.getString(errorRes)
    } else {
        ""
    }
}
