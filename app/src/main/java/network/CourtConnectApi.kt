package com.example.courtconnect.network

import com.example.courtconnect.model.Match
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CourtConnectApi {
    @GET("api/matches")
    suspend fun getMatches(): List<Match>

    @POST("api/matches")
    suspend fun createMatch(@Body match: Match): Match

    @PUT("api/matches/{id}")
    suspend fun updateMatch(@Path("id") id: Int, @Body match: Match): Match

    @DELETE("api/matches/{id}")
    suspend fun deleteMatch(@Path("id") id: Int)
}