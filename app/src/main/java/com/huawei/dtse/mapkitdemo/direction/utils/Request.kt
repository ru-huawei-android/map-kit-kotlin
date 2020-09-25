package com.huawei.dtse.mapkitdemo.direction.utils

import android.util.Log
import com.huawei.hms.maps.model.LatLng
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "Request"

fun requestBody(origin: LatLng, destination: LatLng): RequestBody {

    val json = JSONObject()
    val originJson = JSONObject()
    val destinationJson = JSONObject()

    val type = MediaType.parse("application/json; charset=utf-8")

    originJson.put("lat", origin.latitude)
    originJson.put("lng", origin.longitude)

    destinationJson.put("lat", destination.latitude)
    destinationJson.put("lng", destination.longitude)

    json.put("origin", originJson)
    json.put("destination", destinationJson)

    Log.d(TAG, json.toString())
    return RequestBody.create(type, json.toString())
}

fun requestBody(origin: LatLng, destination: LatLng, alternatives: Boolean, avoid: Array<Int?>):
        RequestBody {

    val json = JSONObject()
    val originJson = JSONObject()
    val destinationJson = JSONObject()
    val avoidJson = JSONArray()

    val type = MediaType.parse("application/json; charset=utf-8")

    originJson.put("lat", origin.latitude)
    originJson.put("lng", origin.longitude)

    destinationJson.put("lat", destination.latitude)
    destinationJson.put("lng", destination.longitude)

    for (item in avoid) {
        if (item == 1 || item == 2) avoidJson.put(item)
    }

    json.put("origin", originJson)
    json.put("destination", destinationJson)
    json.put("alternatives", alternatives)
    json.put("avoid", avoidJson)

    Log.d(TAG, json.toString())
    return RequestBody.create(type, json.toString())
}
