package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.data.model.AuthObjects
import com.example.winest_aplication.data.network.AuthService
import com.example.winest_aplication.databinding.ActivityLoginBinding
import com.example.winest_aplication.data.network.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authService: AuthService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginFlow()
    }

    private fun loginFlow() {
        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val response = authService.login(AuthObjects.LoginRequest(email, password))

                if (response.isSuccessful) {
                    val tokenManager: TokenManager by inject()

                    tokenManager.token = "${response.body()?.jwt}"
                    startActivity(Intent(this@LoginActivity, FeedActivity::class.java))
                    Log.i("APIStatus", "Ok  ${response.body()?.jwt}")
                } else {
                    Log.e("APIStatus", "Error")
                }
            }
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
