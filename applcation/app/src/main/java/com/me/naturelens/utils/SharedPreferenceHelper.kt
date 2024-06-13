//import android.content.Context
//import android.content.SharedPreferences
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//
//object SharedPreferenceHelper {
//
//    private const val PREFERENCE_NAME = "MyAppPreferences"
//
//    private var sharedPreferences: SharedPreferences? = null
//
//    fun initialize(context: Context) {
//        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
//    }
//
//    fun saveIntegerList(key: String, integerList: List<Int>) {
//        val editor = sharedPreferences?.edit()
//        val gson = Gson()
//        val json = gson.toJson(integerList)
//        editor?.putString(key, json)
//        editor?.apply()
//    }
//
//    fun getIntegerList(key: String): List<Int>? {
//        val json = sharedPreferences?.getString(key, null)
//        val type = object : TypeToken<List<Int?>?>() {}.type
//        return Gson().fromJson(json, type)
//    }
//}
package com.me.naturelens.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferenceHelper {

    private const val PREFERENCE_NAME = "MyAppPreferences"

    private var sharedPreferences: SharedPreferences? = null

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun saveIntegerList(key: String, integerList: MutableList<Int>) {
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val json = gson.toJson(integerList)
        editor?.putString(key, json)
        editor?.apply()
    }

    fun getIntegerList(key: String): MutableList<Int>? {
        val json = sharedPreferences?.getString(key, null)
        val type = object : TypeToken<MutableList<Int?>?>() {}.type
        return Gson().fromJson(json, type)
    }
}