package com.omarahmed.myassistant.data.remote

import com.google.gson.annotations.SerializedName


data class CountriesResponse(
    val response: Countries
) {
    data class Countries(
        val countries: List<CountriesInfo>
    ){
        data class CountriesInfo(
            @SerializedName("country_name")
            val countryName: String?,
            @SerializedName("iso-3166")
            val isoName: String?,
            @SerializedName("total_holidays")
            val totalHolidays: Int,
            val uuid: String
        )
    }
}