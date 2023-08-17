package com.example.winest_aplication.presentation.uiUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.winest_aplication.R
import com.example.winest_aplication.data.model.PostsObjects
import com.example.winest_aplication.databinding.ItemFeedBinding

class FeedAdapter(
    private val posts: List<PostsObjects.Post>,
    private val userName: String?,
    private val likeClickListener: OnLikeClickListener
) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    interface OnLikeClickListener {
        fun onLikeClick(position: Int)
    }

    inner class FeedViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivFeedLikes.setOnClickListener {
                likePostChange()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    likeClickListener.onLikeClick(position)
                }
            }
        }

        fun bind(post: PostsObjects.Post) = with(binding) {
            tvFeedUsername.text = post.author.name
            tvFeedPostDescription.text = post.content
            tvFeedLikesCount.text = post.Like.size.toString()
            if (userName != null) {
                post.Like.forEach { like ->
                    if (like.user.name == userName) {
                        ivFeedLikes.setImageResource(R.drawable.ic_like_filled)
                    }
                }
            }
            if (post.imgSource != null) {
                Glide.with(ivFeedPostImage.context)
                    .load(post.imgSource)
                    .override(350, 350)
                    .centerCrop()
                    .into(ivFeedPostImage)
            } else {
                ivFeedPostImage.visibility = View.GONE
            }
        }

        fun likePostChange() = with(binding) {
            val totalLikes = tvFeedLikesCount.text.toString().toInt() + 1
            tvFeedLikesCount.text = totalLikes.toString()
            ivFeedLikes.setImageResource(R.drawable.ic_like_filled)
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
