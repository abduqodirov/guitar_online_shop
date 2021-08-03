package com.abduqodirov.guitaronlineshop.data.local_chaching

import android.content.Context
import javax.inject.Inject

private const val SHARED_PREF_FILE_NAME = "keys"
private const val TOKEN_KEY = "TOKEN_SHOP"

class TokenManager @Inject constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPref.getString(TOKEN_KEY, null)
    }

    fun insertToken(token: String) {
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }
}
