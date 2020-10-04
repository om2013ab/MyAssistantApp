package com.omarahmed.myassistant.utils

import android.app.DatePickerDialog
import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*


class DatePicker {
    companion object {
        fun datePickerDialog(view: TextInputEditText, context: Context) {
            val instance = Calendar.getInstance()
            DatePickerDialog(context, { _, year, month, dayOfMonth ->

                    instance.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    view.setText(
                        SimpleDateFormat(DATE_PATTERN, Locale.US).format(instance.time)
                    )

                },
                instance.get(Calendar.YEAR),
                instance.get(Calendar.MONTH),
                instance.get(Calendar.DAY_OF_MONTH)
            ).show()

        }


    }

}