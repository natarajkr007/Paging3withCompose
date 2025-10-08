package com.nataraj.paging3.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */

val jsonConverter = Json {
    ignoreUnknownKeys = true
}

val retrofit: Retrofit by lazy {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    Retrofit.Builder()
        .baseUrl("https://dummyjson.com")
        .client(okHttpClient)
        .addConverterFactory(jsonConverter.asConverterFactory("application/json".toMediaType()))
        .build()
}

val dummyProductsService: DummyProductsService by lazy {
    retrofit.create(DummyProductsService::class.java)
}
