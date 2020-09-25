package com.huawei.dtse.mapkitdemo.direction.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huawei.dtse.mapkitdemo.direction.model.Direction
import com.huawei.hms.maps.model.LatLng
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "HuaweiDetector"
private const val BASE_URL = "https://mapapi.cloud.huawei.com/mapApi/v1/routeService/"
private const val API_KEY = "CgB6e3x9HQFRJOUV0KDIQLrNmQcQC7M8rxo5wvvSa2GozzXB3p9NbwPurIsys8M1jYYyB762er5ZmRdwJb+dbaYP"

class DirectionRepository {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DirectionServiceApi::class.java)

    fun getDrivingDirection(request: RequestBody): LiveData<Direction> {
        val data: MutableLiveData<Direction>  = MutableLiveData()
        api.getDrivingDirection(request, API_KEY)
            .enqueue(object : Callback<Direction> {
                override fun onFailure(call: Call<Direction>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                    data.value = null
                }

                override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                    Log.d(TAG, "onResponse: ${response.body().toString()}")
                    Log.d(TAG, call.request().url().toString())
                    if (response.isSuccessful) data.value = response.body()
                }
            })
        return data
    }

    fun getWalkingDirection(request: RequestBody): LiveData<Direction> {
        val data: MutableLiveData<Direction>  = MutableLiveData()
        api.getWalkingDirection(request, API_KEY)
            .enqueue(object : Callback<Direction> {
                override fun onFailure(call: Call<Direction>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                    data.value = null
                }

                override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                    Log.d(TAG, "onResponse: ${response.body().toString()}")
                    if (response.isSuccessful) data.value = response.body()
                }
            })
        return data
    }

    fun getBicyclingDirection(request: RequestBody): LiveData<Direction> {
        val data: MutableLiveData<Direction>  = MutableLiveData()
        api.getBicyclingDirection(request, API_KEY)
            .enqueue(object : Callback<Direction> {
                override fun onFailure(call: Call<Direction>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                    data.value = null
                }

                override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                    Log.d(TAG, "onResponse: ${response.body().toString()}")
                    if (response.isSuccessful) data.value = response.body()
                }
            })
        return data
    }

    fun getPolyline(direction: Direction): LiveData<MutableMap<Int, List<LatLng>>> {
        val routes = MutableLiveData<MutableMap<Int, List<LatLng>>>()
        val result: MutableMap<Int, List<LatLng>> = HashMap()
        var counter = 0

        if (direction.returnDesc == "OK") {
            for (route in direction.routes) {
                val polylinePoints: MutableList<LatLng> = ArrayList()
                val steps = route.paths[0].steps
                for (step in steps) {
                    for (polylineItem in step.polyline) {
                        polylinePoints.add(LatLng(polylineItem.lat, polylineItem.lng))
                    }
                }
                result[counter++] = polylinePoints
            }
        }
        routes.value = result
        return routes
    }
}