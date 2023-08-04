package com.example.winest_aplication.presentation.uiUtils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.winest_aplication.databinding.ItemFeedBinding
import com.example.winest_aplication.data.model.PostsObjects

class FeedAdapter(private val posts: List<PostsObjects.Post>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    class FeedViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostsObjects.Post) {
            binding.authorName.text = post.author.name
            binding.postContent.text = post.content
            Glide.with(binding.postImage.context).load(post.imgSource).into(binding.postImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBinding.inflate(inflater, parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post: PostsObjects.Post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size
}
