package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winest_aplication.databinding.ActivityFeedBinding
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.presentation.uiUtils.FeedAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private val apiService: PostService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.postsRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.newPostButton.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getPosts(0, 20)

            if (response.isSuccessful) {
                val posts = response.body()?.posts
                runOnUiThread {
                    binding.postsRecyclerView.adapter = FeedAdapter(posts ?: emptyList())
                }
                Log.e("APIStatusFeed", "Error: ${response.body()?.posts}")
            } else {
                Log.e("APIStatusFeed", "Error: ${response.code()}")
            }
        }
    }
}
