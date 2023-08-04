package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.data.model.AuthObjects
import com.example.winest_aplication.data.network.AuthService
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
        buttonCadastrar.setOnClickListener {
            val email = editTextEmail.text.toString()
            val senha = editTextSenha.text.toString()
            val name = editTextNome.text.toString()
            val dataDeNascimento = editTextDataNascimento.text.toString()
            val phone = editTextTelefone.text.toString()
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
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Log.i("APIStatus", "Ok")
                } else {
                    Log.e("APIStatus", "Error")
                }

            }
        }
    }
}
