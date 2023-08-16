package com.example.winest_aplication.data.network

import android.content.Context

class TokenManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    var token: String?
        get() = sharedPreferences.getString("token", null)
        set(value) = sharedPreferences.edit().putString("token", value).apply()

    fun clearToken() {
        sharedPreferences.edit().remove("token").apply()
    }
}
