package com.example.winest_aplication.data.model

object PostsObjects {
    data class CreatePost(
        val id: Int,
        val content: String,
        val authorId: Int,
        val imgSource: String
    )

    data class PostsResponse(
        val posts: List<Post>
    )

    data class Post(
        val id: Int,
        val createdAt: String,
        val imgSource: String?,
        val content: String,
        val author: Author,
        /*val Comment: List<Comment>,
        val Like: List<Like>*/
    )

    data class Author(
        val id: Int,
        val name: String,
        val profilePictureUrl: String?
    )
}