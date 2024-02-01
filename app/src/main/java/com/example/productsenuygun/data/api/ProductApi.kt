package com.example.productsenuygun.data.api

import com.example.productsenuygun.data.model.ProductsResponse
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): ProductsResponse
}