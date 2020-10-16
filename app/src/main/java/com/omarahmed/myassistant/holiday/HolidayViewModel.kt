package com.omarahmed.myassistant.holiday

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.myassistant.MyApplication
import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.data.models.HolidayInfo
import com.omarahmed.myassistant.data.remote.CountriesResponse
import com.omarahmed.myassistant.data.remote.HolidayResponse
import com.omarahmed.myassistant.data.repository.HolidayRepository
import com.omarahmed.myassistant.utils.Resource
import com.omarahmed.myassistant.utils.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HolidayViewModel(application: android.app.Application) : AndroidViewModel(application) {
    private val sharedPreference = SharedPreference(application.applicationContext)
    private val db = CoursesDatabase.getDatabase(application.applicationContext)
    private val repository = HolidayRepository(db)

    private val _holidaysApiResponse: MutableLiveData<Resource<HolidayResponse>> = MutableLiveData()
    val holidaysApiResponse: LiveData<Resource<HolidayResponse>>
        get() = _holidaysApiResponse

    private val _countriesApiResponse: MutableLiveData<Resource<CountriesResponse>> = MutableLiveData()
    val countriesApiResponse: LiveData<Resource<CountriesResponse>>
        get() = _countriesApiResponse

    val countriesFromRoom: LiveData<List<CountriesResponse.Countries.CountriesInfo>> =
        repository.getCountriesFromRoom
    val holidaysFromRoom: LiveData<List<HolidayInfo>> = repository.getHolidaysFromRoom

    fun getHolidaysFromApi(countryCode: String?, year: Int, month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _holidaysApiResponse.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val response = repository.getAllHolidaysFromApi(countryCode, year, month)
                    _holidaysApiResponse.postValue(handleHolidayResponse(response))
                } else {
                    _holidaysApiResponse.postValue(Resource.Error("Check Internet Connection"))
                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _holidaysApiResponse.postValue(Resource.Error("Network Failure"))
                    else -> _holidaysApiResponse.postValue(Resource.Error("Something went wrong"))
                }
            }
        }
    }

    fun getCountriesFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            _countriesApiResponse.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val response = repository.getCountriesFromApi()
                    _countriesApiResponse.postValue(handleCountriesResponse(response))
                } else {
                    _countriesApiResponse.postValue(Resource.Error("Check Internet Connection"))
                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _countriesApiResponse.postValue(Resource.Error("Network Failure"))
                    else -> _countriesApiResponse.postValue(Resource.Error("Something went wrong"))
                }
            }
        }
    }



    private fun handleCountriesResponse(response: Response<CountriesResponse>): Resource<CountriesResponse>? {
        if (response.isSuccessful) {
            response.body()?.response?.countries?.forEach {
                insertCountries(it)
            }
            val countriesResponse = response.body()
            countriesResponse?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleHolidayResponse(response: Response<HolidayResponse>): Resource<HolidayResponse> {
        if (response.isSuccessful) {
            val holidaysResponse = response.body()
            holidaysResponse?.let {
                return Resource.Success(it)
            }

        }
        return Resource.Error(response.message())

    }


    private fun insertCountries(countriesInfo: CountriesResponse.Countries.CountriesInfo) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCountries(countriesInfo)
        }

    private fun insertHolidays(holidayInfo: HolidayInfo) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHolidays(holidayInfo)
        }

    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activityNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}