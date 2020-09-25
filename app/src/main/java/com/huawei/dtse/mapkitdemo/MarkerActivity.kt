/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  2020.1.3-Changed modify the import classes type and add some marker demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.dtse.mapkitdemo

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMap.InfoWindowAdapter
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_marker.*
import kotlinx.android.synthetic.main.custom_info_window.view.*

private const val TAG = "MarkerActivity"

class MarkerActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    //HUAWEI map
    private var map: HuaweiMap? = null

    //List for markers
    private var markerList: MutableList<Marker?> = ArrayList()

    private var isRestore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker)

        isRestore = savedInstanceState != null

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(com.huawei.dtse.mapkitdemo.utils.MAPVIEW_BUNDLE_KEY)
        }

        mapView.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@MarkerActivity)
        }

        button_add_markers.setOnClickListener(this)
        button_remove_markers.setOnClickListener(this)
    }

    override fun onMapReady(map: HuaweiMap?) {
        Log.d(TAG, "onMapReady: ")
        this.map = map
        this.map?.let {
            it.apply {
                isMyLocationEnabled = true // Enable the my-location overlay.
                uiSettings.isMyLocationButtonEnabled = true // Enable the my-location icon.
                setMarkersClustering(true) // Enable the clustering.
                setInfoWindowAdapter(CustomInfoWindowAdapter()) //Custom info window, not necessary
            }

            addMarkerListener()
            addMapListener()
        }

        if (!isRestore) {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(START_POINT, 4f))
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_markers -> {
                Log.i(TAG, "onClick: add markers")
                addCites()
            }
            R.id.button_remove_markers -> {
                Log.i(TAG, "onClick: remove markers")
                for (marker in markerList) marker?.remove() //map?.clear() - also can be used
                markerList.clear()
            }
        }
    }

    /**
     * Set the listener associated with the marker
     */
    private fun addMarkerListener() {
        map?.let {
            it.setOnInfoWindowClickListener {
                Toast.makeText(applicationContext, "onInfoWindowClickListener", Toast.LENGTH_SHORT).show()
            }
            it.setOnInfoWindowCloseListener {
                Toast.makeText(applicationContext, "onInfoWindowClose", Toast.LENGTH_SHORT).show()
            }
            it.setOnInfoWindowLongClickListener {
                Toast.makeText(applicationContext, "onInfoWindowLongClick", Toast.LENGTH_SHORT).show()
            }
            it.setOnMarkerDragListener(object : HuaweiMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                    Log.i(TAG, "onMarkerDragStart: ${marker.id}")
                }

                override fun onMarkerDrag(marker: Marker) {
                    Log.i(TAG, "onMarkerDrag: ${marker.id}")
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    Log.i(TAG, "onMarkerDragEnd: ${marker.id}")
                }
            })
        }
    }

    /**
     * Set the listener associated with the map
     */
    private fun addMapListener() {
        map?.setOnMapLongClickListener {
            addAddressMarker(it)
            Log.i(TAG, "markerList size is: ${markerList.size}")
        }
    }

    private fun addAddressMarker(latLng: LatLng) {
        val marker = map?.addMarker(MarkerOptions()
            .position(latLng)
            .title(getString(R.string.marker_name))
            .snippet(getString(R.string.snippet))
            .draggable(true)
            .clusterable(true))
        markerList.add(marker)
    }

    private fun addCites() {
        map?.let {
            var marker = it.addMarker(MarkerOptions()
                .position(SAINT_PETERSBURG)
                .title(getString(R.string.SPB))
                .snippet(getString(R.string.russia))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .clusterable(true))
            markerList.add(marker)
            marker = it.addMarker(MarkerOptions()
                .position(MOSCOW)
                .title(getString(R.string.moscow))
                .snippet(getString(R.string.russia))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .clusterable(true))
            markerList.add(marker)
            marker = it.addMarker(MarkerOptions()
                .position(SOCHI)
                .title(getString(R.string.sochi))
                .snippet(getString(R.string.russia))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .clusterable(true))
            markerList.add(marker)
        }
    }

    /**
     * Custom Info Window
     */
    internal inner class CustomInfoWindowAdapter : InfoWindowAdapter {
        private val windowView: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        private val contentsView: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoWindow(marker: Marker): View {
            Log.d(TAG, "getInfoWindow")
            render(marker, windowView)
            return windowView
        }

        override fun getInfoContents(marker: Marker): View {
            Log.d(TAG, "getInfoContents")
            render(marker, windowView)
            return contentsView
        }

        private fun render(marker: Marker, view: View) {
            setMarkerBadge(marker, view)
            setMarkerTitle(marker, view)
            setMarkerSnippet(marker, view)
        }

        private fun setMarkerBadge(marker: Marker, view: View) {
            //TODO
            view.imgv_badge.setImageResource(R.drawable.ic_baseline_map_24)
        }

        private fun setMarkerTitle(marker: Marker, view: View) {
            val markerTitle = marker.title

            if (markerTitle == null) {
                view.txtv_title.text = ""
            } else {
                val titleText = SpannableString(markerTitle)
                titleText.setSpan(ForegroundColorSpan(Color.BLUE), 0, titleText.length, 0)
                view.txtv_title.text = titleText
            }
        }

        private fun setMarkerSnippet(marker: Marker, view: View) {
            val markerSnippet = marker.snippet
            if (markerSnippet != null && markerSnippet.isNotEmpty()) {
                val snippetText = SpannableString(markerSnippet)
                snippetText.setSpan(ForegroundColorSpan(Color.RED), 0, markerSnippet.length, 0)
                view.txtv_snippet.text = snippetText
            } else {
                view.txtv_snippet.text = ""
            }
        }

    }

    companion object {
        private val SAINT_PETERSBURG = LatLng(59.939095, 30.315868)
        private val MOSCOW = LatLng(55.755814, 37.617635)
        private val SOCHI = LatLng(43.585525, 39.723062)
        private val START_POINT = LatLng(54.0, 33.0)
    }
}