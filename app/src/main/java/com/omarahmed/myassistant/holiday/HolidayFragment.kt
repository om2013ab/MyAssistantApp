package com.omarahmed.myassistant.holiday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.data.remote.HolidayResponse
import com.omarahmed.myassistant.utils.Constants.Companion.CURRENT_YEAR
import com.omarahmed.myassistant.utils.Resource
import com.omarahmed.myassistant.utils.SharedPreference
import kotlinx.android.synthetic.main.chip_months_layout.view.*
import kotlinx.android.synthetic.main.fragment_holiday.*
import kotlinx.android.synthetic.main.fragment_holiday.view.*

class HolidayFragment : Fragment() {

    private val holidayViewModel: HolidayViewModel by viewModels()
    private lateinit var holidayAdapter: HolidayAdapter
    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_holiday, container, false)
        holidayAdapter = HolidayAdapter()
        sharedPreference = SharedPreference(requireContext())
        val countryCode = sharedPreference.getStringValue("countryCode","MY")
        val countryName = sharedPreference.getStringValue("countryName","Malaysia")
        holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,1)
        holidayViewModel.holidaysApiResponse.observe(viewLifecycleOwner, { response ->
            handleResponse(response,countryCode)

        })
        view.rv_holidays.adapter = holidayAdapter

        view.holidayToolbar.title = countryName

        view.pickCountry.setOnClickListener {
            findNavController().navigate(R.id.action_holidayFragment_to_countriesFragment)
        }
        view.chip_group.check(R.id.chipJan)
        handleFirstTimeOfRunning()
        handlePickMonth(view,countryCode)
        return view
    }


    private fun handleFirstTimeOfRunning() {
        val firstRun = sharedPreference.getBooleanValue("isFirstRun",true)
        if (firstRun){
            findNavController().navigate(R.id.action_holidayFragment_to_countriesFragment)
            sharedPreference.putBooleanValue("isFirstRun", false)
        }
    }

    private fun handleResponse(response: Resource<HolidayResponse>?, countryCode: String?) {
        when (response) {
            is Resource.Success -> {
                progressLoading.visibility = View.INVISIBLE
                response.data?.let { holidayResponse ->
                    holidayAdapter.holidayList.submitList(holidayResponse.response.holidays)
                    if (holidayResponse.response.holidays.isEmpty()){
                        noHolidayTxt.visibility = View.VISIBLE
                        referenceTxt.visibility = View.INVISIBLE
                    }else{
                        noHolidayTxt.visibility = View.INVISIBLE
                        referenceTxt.visibility = View.VISIBLE
                    }
                }
            }
            is Resource.Error -> {
                progressLoading.visibility = View.INVISIBLE
                response.message?.let { message ->
                    Snackbar.make(requireView(),message,Snackbar.LENGTH_LONG).apply {
                        setAction("RETRY"){
                            holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,1)
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

    private fun handlePickMonth(view: View,countryCode: String?){
        view.chip_group.setOnCheckedChangeListener { _, checkedId ->
          when(checkedId){
              R.id.chipJan -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,1)
              }
              R.id.chipFeb -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,2)
              }
              R.id.chipMar -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,3)
              }
              R.id.chipApr -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,4)
              }
              R.id.chipMay -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,5)
              }
              R.id.chipJun -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,6)
              }
              R.id.chipJul -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,7)
              }
              R.id.chipAug -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,8)
              }
              R.id.chipSep -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,9)
              }
              R.id.chipOct -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,10)
              }
              R.id.chipNov -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,11)
              }
              R.id.chipDec -> {
                  holidayViewModel.getHolidaysFromApi(countryCode, CURRENT_YEAR,12)
              }
          }
        }
    }


}