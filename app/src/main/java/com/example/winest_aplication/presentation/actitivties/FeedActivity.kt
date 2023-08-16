package com.example.winest_aplication.presentation.actitivties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.databinding.ActivityFeedBinding
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
        refreshLayout()
        createPost()
        loadFeed()
    }

    private fun refreshLayout() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            loadFeed()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun createPost() = with(binding) {
        newPostButton.setOnClickListener {
            val intent = Intent(this@FeedActivity, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFeed() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getPosts(0, 20)

            if (response.isSuccessful) {
                val posts = response.body()?.posts
                runOnUiThread {
                    postsRecyclerView.adapter = FeedAdapter(posts ?: emptyList())
                }
                Log.e("APIStatusFeed", "Error: ${response.body()?.posts}")
            } else if (response.code() in 400..402) {
                val intent = Intent(this@FeedActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("APIStatusFeed", "Error: ${response.code()}")
            }
        }
    }
}
