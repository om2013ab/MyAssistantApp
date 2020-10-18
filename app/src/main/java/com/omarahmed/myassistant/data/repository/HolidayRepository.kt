package com.omarahmed.myassistant.data.repository

import com.omarahmed.myassistant.data.remote.RetrofitInstance
import com.omarahmed.myassistant.utils.Constants.Companion.API_KEY

class HolidayRepository() {

    // api
    suspend fun getAllHolidaysFromApi(countryCode:String?, year:Int, month: Int) =
        RetrofitInstance.api.getAllHolidays(API_KEY,countryCode,year,month)

    suspend fun getCountriesFromApi() = RetrofitInstance.api.getAllCountries(API_KEY)

}