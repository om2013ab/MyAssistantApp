package com.omarahmed.myassistant.data.remote

import com.omarahmed.myassistant.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())
        .build()

    val api: HolidayApi by lazy {
        retrofit.create(HolidayApi::class.java)
    }
}