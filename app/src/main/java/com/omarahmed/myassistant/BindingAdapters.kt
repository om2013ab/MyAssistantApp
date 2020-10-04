package com.omarahmed.myassistant

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.Constants.Companion.TIME_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {
    companion object {
        @BindingAdapter("setVisibility")
        @JvmStatic
        fun notificationIconVisibility(imageView: ImageView, isChecked: Boolean) {
            if (isChecked) {
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("checkEmpty")
        @JvmStatic
        fun checkEmpty(view:View, noData:LiveData<Boolean>?){
            noData?.let {
                when(it.value){
                    true -> view.visibility = View.VISIBLE
                    false -> view.visibility = View.INVISIBLE
                }
            }
        }

        @BindingAdapter("dateFormat")
        @JvmStatic
        fun setDateFormat(view: TextView, date: Date) {
            val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)
            view.text = dateFormat.format(date)
        }

        @BindingAdapter("timeFormat")
        @JvmStatic
        fun setTimeFormat(view: TextView, time: Date){
            val timeFormat = SimpleDateFormat(TIME_PATTERN, Locale.US)
            view.text = timeFormat.format(time)
        }

        @BindingAdapter("setText")
        @JvmStatic
        fun adjustHolidayDays(textView: TextView, day: Int){
            if (day in 1 until 10 ){
                textView.text = "0$day"
            }else{
                textView.text = day.toString()
            }
        }
    }
}