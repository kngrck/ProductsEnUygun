package com.example.productsenuygun.data.api

import com.example.productsenuygun.data.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): ProductsResponse
}