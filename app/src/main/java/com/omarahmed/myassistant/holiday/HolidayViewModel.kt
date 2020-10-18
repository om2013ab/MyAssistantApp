package com.omarahmed.myassistant.holiday

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.myassistant.MyApplication
import com.omarahmed.myassistant.data.remote.CountriesResponse
import com.omarahmed.myassistant.data.remote.HolidayResponse
import com.omarahmed.myassistant.data.repository.HolidayRepository
import com.omarahmed.myassistant.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HolidayViewModel(application: android.app.Application) : AndroidViewModel(application) {
    private val repository = HolidayRepository()

    private val _holidaysApiResponse: MutableLiveData<Resource<HolidayResponse>> = MutableLiveData()
    val holidaysApiResponse: LiveData<Resource<HolidayResponse>>
        get() = _holidaysApiResponse

    private val _countriesApiResponse: MutableLiveData<Resource<CountriesResponse>> = MutableLiveData()
    val countriesApiResponse: LiveData<Resource<CountriesResponse>>
        get() = _countriesApiResponse


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

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activityNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}