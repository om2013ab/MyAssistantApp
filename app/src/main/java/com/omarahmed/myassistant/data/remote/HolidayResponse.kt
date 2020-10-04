package com.omarahmed.myassistant.data.remote

import com.omarahmed.myassistant.data.models.HolidayInfo


data class HolidayResponse(
    val response: Holidays
) {
    data class Holidays(
        val holidays: List<HolidayInfo>
    )
}