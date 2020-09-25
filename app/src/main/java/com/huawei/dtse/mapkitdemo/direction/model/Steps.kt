package com.huawei.dtse.mapkitdemo.direction.model

import com.google.gson.annotations.SerializedName

data class Steps (

    @SerializedName("duration") val duration : Double,
    @SerializedName("orientation") val orientation : Int,
    @SerializedName("durationText") val durationText : String,
    @SerializedName("distance") val distance : Double,
    @SerializedName("startLocation") val startLocation : StartLocation,
    @SerializedName("instruction") val instruction : String,
    @SerializedName("action") val action : String,
    @SerializedName("distanceText") val distanceText : String,
    @SerializedName("endLocation") val endLocation : EndLocation,
    @SerializedName("polyline") val polyline : List<Polyline>,
    @SerializedName("roadName") val roadName : String
)