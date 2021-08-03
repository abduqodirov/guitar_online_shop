package com.abduqodirov.guitaronlineshop.data.repository.auth

import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) : AuthRepository {

    override fun login(email: String, password: String) = flow {
        emit(Response.Loading)
        // TODO: 8/2/2021 In case of wrong login or password extract message from response and emit to Failure, instead of http exception
        try {
            emit(Response.Success(remoteDataSource.loginWithEmail(email, password)))
        } catch (e: Exception) {
            emit(Response.Failure(e.localizedMessage))
        }
    }

    override fun logout(email: String) {
    }

    override fun signUp(email: String, password: String) = flow {
        emit(Response.Loading)
        try {
            // TODO: 8/3/2021 In case of repetitive email or something failing, extract message from response
            // and emit to Failure instead of http exception
            emit(Response.Success(remoteDataSource.signUpWithEmail(email, password)))
        } catch (e: Exception) {
            emit(Response.Failure(e.localizedMessage))
        }
    }
}
