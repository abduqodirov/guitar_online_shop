package com.abduqodirov.guitaronlineshop.view.screens.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.data.local_chaching.TokenManager
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepository
import com.abduqodirov.guitaronlineshop.view.model.Validation
import com.abduqodirov.guitaronlineshop.view.util.MAXIMUM_PASSWORD_LENGTH
import com.abduqodirov.guitaronlineshop.view.util.MINIMUM_PASSWORD_LENGTH
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) :
    ViewModel() {

    private val _user = MutableLiveData<Response<TokenUserDTO>>()
    val user: LiveData<Response<TokenUserDTO>> get() = _user

    private val _formValidations = MutableLiveData(
        arrayOf(
            Validation(EDITTEXT_SIGN_UP_EMAIL_POSITION, validator = ::emailValidator),
            Validation(EDITTEXT_SIGN_UP_PASSWORD_POSITION, validator = ::passwordValidator),
            Validation(EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION, validator = ::passwordValidator)
        )
    )
    val formValidations: LiveData<Array<Validation>> get() = _formValidations

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signUp(email, password).collect {
                _user.postValue(it)
            }
        }
    }

    fun saveToken(token: String) {
        tokenManager.insertToken(token)
    }

    fun validatePassword(
        position: Int,
        siblingPosition: Int,
        password: String,
        siblingPassword: String
    ) {
        val oldValidation = formValidations.value

        var result = passwordValidator(password)

        // Checks for matching only a single password follows the password guideline.
        // It haves the user understand if the user entered invalid password.
        if (result == null) {
            if (password != siblingPassword) {
                result = R.string.validation_not_matching_passwords
            }
        }

        val validationField = oldValidation?.get(position)
        val siblingValidationField = oldValidation?.get(siblingPosition)

        if (validationField != null) {

            validationField.errorResId = result
            oldValidation[position] = validationField

            if (result == null) {
                if (siblingValidationField != null) {
                    siblingValidationField.errorResId = null
                    oldValidation[siblingPosition] = siblingValidationField
                }
            }

            _formValidations.value = oldValidation
        }
    }

    // TODO The same with SubmitProductViewModel. So extract to some class. And use it from there
    fun validateEditText(
        position: Int,
        text: String
    ) {
        val oldValidation = formValidations.value
        val validatorFunction = oldValidation?.get(position)?.validator
        val result = validatorFunction?.invoke(text)

        val validationField = oldValidation?.get(position)

        if (validationField != null) {

            validationField.errorResId = result
            oldValidation[position] = validationField

            _formValidations.value = oldValidation
        }
    }

    private fun emailValidator(email: String): Int? {
        if (email.isEmpty()) {
            return R.string.validation_empty_field
        }

        val regex =
            Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")

        if (!regex.matches(email.lowercase())) {
            return R.string.validation_not_email
        }

        return null
    }

    // TODO repetitive code
    private fun passwordValidator(password: String): Int? {
        if (password.isEmpty()) {
            return R.string.validation_empty_field
        }

        if (password.length < MINIMUM_PASSWORD_LENGTH) {
            return R.string.validation_short_password
        }

        if (password.length > MAXIMUM_PASSWORD_LENGTH) {
            return R.string.validation_long_password
        }

        if (password.contains(" ")) {
            return R.string.validation_password_with_space
        }

        return null
    }
}
