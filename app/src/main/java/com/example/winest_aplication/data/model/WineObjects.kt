package com.example.winest_aplication.data.model

object WineObjects {

    data class WineListResponse(
        val wines: List<WineResponse>
    )

    data class WineResponse(
        val id: Int,
        val points: String,
        val title: String,
        val description: String,
        val tasterName: String,
        val tasterTwitterHandle: String,
        val price: String?,
        val designation: String,
        val variety: String,
        val region1: String?,
        val region2: String?,
        val province: String,
        val country: String,
        val winery: String,
        val createdAt: String
    )

}