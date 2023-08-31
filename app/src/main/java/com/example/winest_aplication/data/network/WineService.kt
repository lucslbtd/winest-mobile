package com.example.winest_aplication.data.network

import com.example.winest_aplication.data.model.WineObjects
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WineService {

    @GET("/wines")
    suspend fun getWines(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String
    ): Response<List<WineObjects.WineResponse>>

    @POST("/wines/sugest")
    suspend fun gptPrompt(): Response<WineObjects.GPTPrompt>

    @POST("/favorites/wines/{id}")
    suspend fun favoriteWine(@Path("id") wineId: Int) : Response<Unit>

    @DELETE("/favorites/wines/{id}")
    suspend fun deleteFromFavorites(@Path("id") wineId: Int) : Response<Unit>

    @GET("/favorites/wines")
    suspend fun getFavoriteWines() : Response<WineObjects.UserFavoriteWines>
 }
