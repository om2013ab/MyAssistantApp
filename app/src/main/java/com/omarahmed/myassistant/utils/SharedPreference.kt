package com.omarahmed.myassistant.utils

import android.content.Context

class SharedPreference(context: Context) {
    private val sharedPreference = context.getSharedPreferences("pref",Context.MODE_PRIVATE)

    fun getStringValue(key:String, defValue:String) = sharedPreference.getString(key,defValue)
    fun putStringValue(key: String, value: String?) = sharedPreference.edit().putString(key,value).apply()
    fun getBooleanValue(key:String, defValue:Boolean) = sharedPreference.getBoolean(key,defValue)
    fun putBooleanValue(key: String, value: Boolean) = sharedPreference.edit().putBoolean(key,value).apply()
}