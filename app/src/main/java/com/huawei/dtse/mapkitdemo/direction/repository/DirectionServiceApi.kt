package com.huawei.dtse.mapkitdemo.direction.repository

import com.huawei.dtse.mapkitdemo.direction.model.Direction
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

private const val DRIVING = "driving"
private const val BICYCLING = "bicycling"
private const val WALKING = "walking"

interface DirectionServiceApi {

    @POST(DRIVING)
    fun getDrivingDirection(@Body body: RequestBody, @Query("key") apiKey: String): Call<Direction>

    @POST(BICYCLING)
    fun getBicyclingDirection(@Body body: RequestBody, @Query("key") apiKey: String): Call<Direction>

    @POST(WALKING)
    fun getWalkingDirection(@Body body: RequestBody, @Query("key") apiKey: String): Call<Direction>
}