package com.omarahmed.myassistant.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApi {

    @GET("api/v2/holidays")
    suspend fun getAllHolidays(
        @Query("api_key") apiKey: String,
        @Query("country") countryCode: String?,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<HolidayResponse>

    @GET("api/v2/countries")
    suspend fun getAllCountries(
        @Query("api_key") apiKey: String
    ): Response<CountriesResponse>
}