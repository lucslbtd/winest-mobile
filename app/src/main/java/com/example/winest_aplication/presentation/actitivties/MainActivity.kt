package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.R
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.databinding.ActivityMainBinding
import com.example.winest_aplication.presentation.fragments.FeedFragment
import com.example.winest_aplication.presentation.fragments.WinesFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val apiService: PostService by inject()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validateConnection()

        binding.bnbFeed.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    val fragment = FeedFragment()
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    true
                }
                R.id.action_search -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    val fragment = WinesFragment()
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    true
                }
                R.id.action_profile -> {
                    val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                // Add more cases for other menu items if needed
                else -> false
            }
        }
    }

    private fun validateConnection() = with(binding) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiService.getPosts(0, 1)
                if (response.isSuccessful) {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    val fragment = FeedFragment()
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else if (response.code() in 400..402) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("APIStatusFeed", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("APIStatus", "Error $e")
            }
        }
    }
}
