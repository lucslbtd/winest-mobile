package com.example.winest_aplication.presentation.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.databinding.FragmentFeedBinding
import com.example.winest_aplication.presentation.actitivties.CreatePostActivity
import com.example.winest_aplication.presentation.actitivties.LoginActivity
import com.example.winest_aplication.presentation.uiUtils.FeedAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private val apiService: PostService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postsRecyclerView.layoutManager = LinearLayoutManager(context)
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
        fabFeedCreatePost.setOnClickListener {
            val intent = Intent(context, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFeed() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getPosts(0, 20)

            if (response.isSuccessful) {
                val posts = response.body()?.posts
                val sharedPreferences =
                    requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                val userName = sharedPreferences.getString("userName", null)

                view?.post {
                    val adapter = FeedAdapter(
                        posts ?: emptyList(),
                        userName,
                        object : FeedAdapter.OnLikeClickListener {
                            override fun onLikeClick(position: Int) {
                                val postId = posts?.get(position)?.id
                                if (postId != null) {
                                    likePost(postId)
                                }
                            }
                        }
                    )
                    postsRecyclerView.adapter = adapter
                }
            } else if (response.code() in 400..402) {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("APIStatusFeed", "Error: ${response.code()}")
            }
        }
    }

    private fun likePost(postId: Int) = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.likePost(postId)
            if (response.isSuccessful) {
                Log.e("APIStatusLike", "Ok: ${response.code()}")
            } else {
                Log.e("APIStatusLike", "Error: ${response.code()}")
            }
        }
    }
}
