package com.example.winest_aplication.data.model

object AuthObjects {
    data class LoginRequest(val email: String, val password: String)

    data class SignUpRequest(val email: String, val password: String, val name: String, val birthday: String, val phone: String)

    data class LoginResponse(
        val user: User,
        val jwt: String
    )

    data class User(
        val id: Int,
        val name: String,
        val email: String
    )

}