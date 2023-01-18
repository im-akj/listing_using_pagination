package com.listingwithpagination.api

import com.listingwithpagination.BuildConfig
import com.listingwithpagination.model.ResponseApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("${BuildConfig.BASE_URL}products?")
    suspend fun getProducts(
        @Query("page") page: Int
    ): Response<ResponseApi>
}