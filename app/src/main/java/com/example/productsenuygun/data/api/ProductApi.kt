package com.example.productsenuygun.data.api

import com.example.productsenuygun.data.model.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): List<Product>
}