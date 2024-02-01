package com.example.productsenuygun.di

import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.data.repository.ProductRepositoryImpl
import com.example.productsenuygun.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationContext::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository {
        return ProductRepositoryImpl(api)
    }
}
