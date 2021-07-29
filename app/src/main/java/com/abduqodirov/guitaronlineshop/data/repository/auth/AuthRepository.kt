package com.abduqodirov.guitaronlineshop.data.repository.auth

import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(email: String, password: String): Flow<Response<TokenUserDTO>>

    fun logout(email: String)

    fun signUp(email: String, password: String)
}
