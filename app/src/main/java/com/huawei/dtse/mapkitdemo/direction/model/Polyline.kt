package com.huawei.dtse.mapkitdemo.direction.model

import com.google.gson.annotations.SerializedName

data class Polyline (

	@SerializedName("lng") val lng : Double,
	@SerializedName("lat") val lat : Double
)