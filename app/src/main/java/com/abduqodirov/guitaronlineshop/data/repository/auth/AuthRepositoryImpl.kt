package com.abduqodirov.guitaronlineshop.data.repository.auth

import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    AuthRepository {

    override fun login(email: String, password: String) = flow {
        emit(Response.Loading)
        emit(remoteDataSource.loginWithEmail(email, password))
        // TODO: 8/6/2021 Save token with Token Manager
    }

    override fun logout(email: String) {
        // TODO: 8/6/2021 Delete token with Token Manager
    }

    override fun signUp(email: String, password: String) = flow {
        emit(Response.Loading)
        emit(remoteDataSource.signUpWithEmail(email, password))
        // TODO: 8/6/2021 Save token with Token Manager
    }

    override fun refreshToken(): Flow<Response<TokenUserDTO>> = flow {
        emit(Response.Loading)
        emit(remoteDataSource.refreshToken())
    }
}
