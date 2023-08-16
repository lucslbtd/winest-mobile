package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.data.model.AuthObjects
import com.example.winest_aplication.data.network.AuthService
import com.example.winest_aplication.data.network.TokenManager
import com.example.winest_aplication.networkModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    private val authService: AuthService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(listOf(networkModule)) // Adicione todos os outros módulos que você tiver aqui
        }



        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
    }

}
