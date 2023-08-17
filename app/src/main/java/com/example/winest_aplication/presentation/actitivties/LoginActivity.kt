package com.example.winest_aplication.presentation.actitivties

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.data.model.AuthObjects
import com.example.winest_aplication.data.network.AuthService
import com.example.winest_aplication.data.network.TokenManager
import com.example.winest_aplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authService: AuthService by inject()
    private val tokenManager: TokenManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager.clearToken()
        loginFlow()
    }

    private fun loginFlow() = with(binding) {
        btnLogin.setOnClickListener {
            val email = edtEmailLogin.text.toString()
            val password = edtPasswordLogin.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val response = authService.login(AuthObjects.LoginRequest(email, password))

                if (response.isSuccessful) {
                    tokenManager.token = "${response.body()?.jwt}"
                    saveUserInfo(response)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    Log.i("APIStatus", "Ok  ${response.body()?.jwt}")
                } else {
                    Log.e("APIStatus", "Error")
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserInfo(responseData: Response<AuthObjects.LoginResponse>){
        val sharedPreferences =
            getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", responseData.body()?.user?.name)
        editor.putString("userEmail", responseData.body()?.user?.email)
        editor.putString("userPhone", responseData.body()?.user?.phone)
        editor.putString("userBirthday", responseData.body()?.user?.birthday)
        editor.putString("userBio", responseData.body()?.user?.bio)
        editor.apply()
    }

}
