package com.abduqodirov.guitaronlineshop.view.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _user = MutableLiveData<Response<TokenUserDTO>>()
    val user: LiveData<Response<TokenUserDTO>> get() = _user

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect {
                _user.postValue(it)
            }
        }
    }
}
