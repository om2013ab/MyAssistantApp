package com.omarahmed.myassistant.data.remote

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CountriesResponse(
    val response: Countries
) {
    data class Countries(
        val countries: List<CountriesInfo>
    ){
        @Parcelize
        @Entity(tableName = "countries_table")
        data class CountriesInfo(
            @SerializedName("country_name")
            val countryName: String?,
            @SerializedName("iso-3166")
            val isoName: String?,
            @SerializedName("total_holidays")
            val totalHolidays: Int,
            @PrimaryKey
            val uuid: String
        ):Parcelable
    }
}