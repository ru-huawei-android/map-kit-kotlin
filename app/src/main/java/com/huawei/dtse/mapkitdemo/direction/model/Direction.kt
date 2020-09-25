package com.huawei.dtse.mapkitdemo.direction.model

import com.google.gson.annotations.SerializedName

data class Direction (

	@SerializedName("routes") val routes : List<Route>,
	@SerializedName("returnCode") val returnCode : Int,
	@SerializedName("returnDesc") val returnDesc : String
)