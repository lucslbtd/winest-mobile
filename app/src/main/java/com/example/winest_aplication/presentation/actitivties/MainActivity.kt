package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(listOf(networkModule)) // Adicione todos os outros módulos que você tiver aqui
        }

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
