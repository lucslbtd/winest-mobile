package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.data.model.AuthObjects
import com.example.winest_aplication.data.network.AuthService
import com.example.winest_aplication.data.network.TokenManager
import com.example.winest_aplication.databinding.ActivitySignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val authService: AuthService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerFlow()
    }

    private fun registerFlow() = with(binding) {
        btnSignUp.setOnClickListener {
            val email = edtEmailSignUp.text.toString()
            val senha = edtPasswordSingUp.text.toString()
            val name = edtFullName.text.toString()
            val dataDeNascimento = edtBirthdaySignUp.text.toString()
            val phone = edtPhoneNumberSignUp.text.toString()
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = authService.register(
                        AuthObjects.SignUpRequest(
                            email,
                            senha,
                            name,
                            dataDeNascimento,
                            phone
                        )
                    )
                    if (response.isSuccessful) {
                        val responseLogin =
                            authService.login(AuthObjects.LoginRequest(email, senha))
                        if (responseLogin.isSuccessful) {
                            val tokenManager: TokenManager by inject()
                            tokenManager.token = "${responseLogin.body()?.jwt}"
                            val intent = Intent(this@SignUpActivity, FeedActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        Log.i("APIStatus", "Ok")
                    } else {
                        Log.e("APIStatus", "Error")
                    }
                }
            } catch (e: Exception) {
                Log.e("APIStatus", "Error")
            }
        }
    }
}
