package com.huawei.dtse.mapkitdemo.direction.model

import com.google.gson.annotations.SerializedName

data class Bounds (

	@SerializedName("southwest") val southwest : Southwest,
	@SerializedName("northeast") val northeast : Northeast
)