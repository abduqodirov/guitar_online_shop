package com.abduqodirov.guitaronlineshop.view.screens.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val user = MutableLiveData<Response<Any>>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect {
            }
        }
    }
}
