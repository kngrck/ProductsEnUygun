package com.example.productsenuygun.di

import android.content.Context
import androidx.room.Room
import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.data.local.AppDatabase
import com.example.productsenuygun.data.repository.CartRepositoryImpl
import com.example.productsenuygun.data.repository.ProductRepositoryImpl
import com.example.productsenuygun.domain.repository.CartRepository
import com.example.productsenuygun.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "products-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository {
        return ProductRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCartRepository(localDatabase: AppDatabase): CartRepository {
        return CartRepositoryImpl(localDatabase)
    }
}
