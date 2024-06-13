package com.me.naturelens

import android.app.Application
import com.google.gson.Gson
import com.me.naturelens.models.ModelPlant
import com.me.naturelens.models.RawPlantsData
import com.me.naturelens.utils.ReadJSONFromAssets
class InitApp: Application() {
    private val gson = Gson()
    lateinit var plantlist:ArrayList<ModelPlant>
    override fun onCreate() {
        super.onCreate()
        val jsonString = ReadJSONFromAssets(context = this,"datainfo.json")
        val data = gson.fromJson(jsonString, RawPlantsData::class.java)
        plantlist= data.toModelPlants(this).toCollection(ArrayList())
    }
}