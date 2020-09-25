package com.huawei.dtse.mapkitdemo.utils

const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

fun <T> append(arr: Array<T>, element: T): Array<T?> {
    val array = arr.copyOf(arr.size + 1)
    array[arr.size] = element
    return array
}
