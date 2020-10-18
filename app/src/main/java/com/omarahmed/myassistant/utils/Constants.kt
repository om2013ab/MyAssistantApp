package com.omarahmed.myassistant.utils

import java.util.*

class Constants {

    companion object{
        const val DATE_PATTERN = "MMM dd, yyyy"
        const val TIME_PATTERN = "hh:mm a"
        const val API_KEY = "829b0a7c193c99bce060859028aa2d9cfad90a35"
        const val BASE_URL = "https://calendarific.com/"
        const val CHANNEL_ID = "my channel"
        var NOTIFICATION_ID = 1
        val CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR)


    }
}