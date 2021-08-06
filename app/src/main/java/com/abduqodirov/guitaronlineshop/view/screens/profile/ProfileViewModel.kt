package com.abduqodirov.guitaronlineshop.view.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.data.local_chaching.UserManager
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _profileData = MutableLiveData<Response<TokenUserDTO>>()
    val profileData: LiveData<Response<TokenUserDTO>> get() = _profileData

    fun fetchProfileData() {
        viewModelScope.launch {
            authRepository.refreshToken().collect {
                _profileData.value = it
            }
        }
    }
}
