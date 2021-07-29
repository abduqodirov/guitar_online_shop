package com.abduqodirov.guitaronlineshop.data.repository.auth

import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) : AuthRepository {

    override fun login(email: String, password: String) = flow {
        emit(Response.Loading)
        try {
            emit(Response.Success(remoteDataSource.loginWithEmail(email, password)))
        } catch (e: Exception) {
            emit(Response.Failure(e.localizedMessage ?: "Failed to load"))
        }
    }

    override fun logout(email: String) {
    }

    override fun signUp(email: String, password: String) {
    }
}
