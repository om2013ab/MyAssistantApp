package com.omarahmed.myassistant.holiday.countries

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.data.remote.CountriesResponse
import com.omarahmed.myassistant.holiday.HolidayViewModel
import com.omarahmed.myassistant.utils.Resource
import com.omarahmed.myassistant.utils.SharedPreference
import kotlinx.android.synthetic.main.fragment_countries.*
import kotlinx.android.synthetic.main.fragment_countries.view.*

class CountriesFragment : Fragment(R.layout.fragment_countries) {
    private val holidayViewModel: HolidayViewModel by viewModels()
    private lateinit var countriesAdapter : CountriesAdapter
    private lateinit var sharedPreference: SharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        countriesAdapter = CountriesAdapter(CountriesAdapter.OnClickListener {countriesInfo ->
            findNavController().navigateUp()
            sharedPreference.putStringValue("countryCode",countriesInfo.isoName)
            sharedPreference.putStringValue("countryName",countriesInfo.countryName)

        })
        holidayViewModel.getCountriesFromApi()
        holidayViewModel.countriesApiResponse.observe(viewLifecycleOwner, { response->
            handleResponse(response)
        })

        view.rv_countries.adapter = countriesAdapter

        setupToolbar(view.countriesToolbar)

    }

    private fun handleResponse(response: Resource<CountriesResponse>?) {
        when(response){
            is Resource.Success -> {
                progressLoading.visibility = View.INVISIBLE
                response.data?.let { countriesResponse ->
                    countriesAdapter.countriesList.submitList(countriesResponse.response.countries)
                }
            }
            is Resource.Error ->{
                response.message?.let { message ->
                    Snackbar.make(requireView(),message,Snackbar.LENGTH_LONG).apply {
                        setAction("Retry"){
                            holidayViewModel.getCountriesFromApi()

                        }
                        show()
                    }
                }
            }
            is Resource.Loading -> {
                progressLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }
    }
}

