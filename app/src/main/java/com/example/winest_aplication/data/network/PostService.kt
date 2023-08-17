package com.example.winest_aplication.data.network

import com.example.winest_aplication.data.model.PostsObjects
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    @GET("/posts")
    suspend fun getPosts(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PostsObjects.PostsResponse>

    @Multipart
    @POST("/posts")
    suspend fun createPost(
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @POST("/posts/{id}/like")
    suspend fun likePost(@Path("id") id: Int): Response<ResponseBody>

    @POST("/post/{id}/comments")
    suspend fun commentPost(@Path("id") id: Int, @Body commentRequest: PostsObjects.CommentRequest): Response<ResponseBody>

}
