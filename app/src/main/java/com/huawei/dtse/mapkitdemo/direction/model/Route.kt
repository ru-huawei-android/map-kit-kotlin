package com.huawei.dtse.mapkitdemo.direction.model

import com.google.gson.annotations.SerializedName
import com.huawei.dtse.mapkitdemo.direction.model.Bounds
import com.huawei.dtse.mapkitdemo.direction.model.Paths

data class Route (

	@SerializedName("paths") val paths : List<Paths>,
	@SerializedName("bounds") val bounds : Bounds
)