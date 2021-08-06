package com.abduqodirov.guitaronlineshop.data.local_chaching

import android.content.Context
import javax.inject.Inject

private const val SHARED_PREF_FILE_NAME = "keys"
private const val TOKEN_ID_KEY = "TOKEN_SHOP"
private const val USER_ID_KEY = "USER_ID_SHOP"

class UserManager @Inject constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPref.getString(TOKEN_ID_KEY, null)
    }

    fun insertToken(token: String) {
        with(sharedPref.edit()) {
            putString(TOKEN_ID_KEY, token)
            apply()
        }
    }

    fun getUserId(): String? {
        return sharedPref.getString(USER_ID_KEY, null)
    }

    fun insertUserId(userId: String) {
        with(sharedPref.edit()) {
            putString(USER_ID_KEY, userId)
            apply()
        }
    }
}
