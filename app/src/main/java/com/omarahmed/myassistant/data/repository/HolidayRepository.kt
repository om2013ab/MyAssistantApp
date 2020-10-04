package com.omarahmed.myassistant.data.repository

import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.data.models.HolidayInfo
import com.omarahmed.myassistant.data.remote.CountriesResponse
import com.omarahmed.myassistant.data.remote.RetrofitInstance
import com.omarahmed.myassistant.utils.Constants.Companion.API_KEY

class HolidayRepository(private val db:CoursesDatabase) {

    // api
    suspend fun getAllHolidaysFromApi(countryCode:String?, year:Int, month: Int) =
        RetrofitInstance.api.getAllHolidays(API_KEY,countryCode,year,month)

    suspend fun getCountriesFromApi() = RetrofitInstance.api.getAllCountries(API_KEY)


    // room
    suspend fun insertCountries(countriesInfo: CountriesResponse.Countries.CountriesInfo){
        db.countriesDao().insertCountries(countriesInfo)
    }

    val getCountriesFromRoom
            = db.countriesDao().getAllCountries()

    suspend fun insertHolidays(holidayInfo: HolidayInfo){
        db.holidayDao().insertHolidays(holidayInfo)
    }

    val getHolidaysFromRoom =
        db.holidayDao().getHolidays()

}