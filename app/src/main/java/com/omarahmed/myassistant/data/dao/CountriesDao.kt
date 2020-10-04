package com.omarahmed.myassistant.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omarahmed.myassistant.data.remote.CountriesResponse


@Dao
interface CountriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countryInfo:CountriesResponse.Countries.CountriesInfo)

    @Query("SELECT * FROM COUNTRIES_TABLE ORDER BY countryName")
    fun getAllCountries(): LiveData<List<CountriesResponse.Countries.CountriesInfo>>


}