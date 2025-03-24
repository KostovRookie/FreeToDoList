package com.kostov.freetodolist.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @GET("v1/images/search")
    suspend fun getRandomCat(
        @Query("limit") limit: Int = 1
    ): List<CatResponse>
}