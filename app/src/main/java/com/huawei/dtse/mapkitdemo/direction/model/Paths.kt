package com.huawei.dtse.mapkitdemo.direction.model

import com.google.gson.annotations.SerializedName

data class Paths (

	@SerializedName("duration") val duration : Double,
	@SerializedName("durationText") val durationText : String,
	@SerializedName("durationInTrafficText") val durationInTrafficText : String,
	@SerializedName("durationInTraffic") val durationInTraffic : Double,
	@SerializedName("distance") val distance : Double,
	@SerializedName("startLocation") val startLocation : StartLocation,
	@SerializedName("startAddress") val startAddress : String,
	@SerializedName("distanceText") val distanceText : String,
	@SerializedName("steps") val steps : List<Steps>,
	@SerializedName("endLocation") val endLocation : EndLocation,
	@SerializedName("endAddress") val endAddress : String
)