package com.example.winest_aplication.data.network

import com.example.winest_aplication.data.model.AuthObjects
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: AuthObjects.LoginRequest): Response<AuthObjects.LoginResponse>

    @POST("/register")
    suspend fun register(@Body signUpRequest: AuthObjects.SignUpRequest): Response<Unit>
}
