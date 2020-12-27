package com.omarahmed.myassistant.utils

import android.app.TimePickerDialog
import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.omarahmed.myassistant.utils.Constants.Companion.TIME_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class TimePicker {
    companion object {

        fun timePickerDialog(view: TextInputEditText, context: Context) {
            val instance = Calendar.getInstance()
            TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.apply {
                        set(Calendar.HOUR_OF_DAY,hourOfDay)
                        set(Calendar.MINUTE,minute)
                    }
                    view.setText(SimpleDateFormat(TIME_PATTERN, Locale.US).format(selectedTime.time))
                },
                instance.get(Calendar.HOUR_OF_DAY),
                instance.get(Calendar.MINUTE),
                false
            ).show()
        }
    }
}