package com.huawei.dtse.mapkitdemo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_big_marker.*
import kotlinx.android.synthetic.main.activity_marker.mapView
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "BigMarkerActivity"
private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

class BigMarkerActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    //HUAWEI map
    private var map: HuaweiMap? = null

    //Map for markers
    private var markers: MutableMap<Long, Marker> = ConcurrentHashMap()

    private var isRestore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_marker)

        isRestore = savedInstanceState != null

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapView.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@BigMarkerActivity)
        }

        button_generate_markers.setOnClickListener(this)
        button_remove_50_markers.setOnClickListener(this)
        button_remove_markers_big.setOnClickListener(this)
        button_clean_map.setOnClickListener(this)
        button_clusterization.setOnClickListener(this)
        button_hide_markers.setOnClickListener(this)
        button_show_markers.setOnClickListener(this)
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

    override fun onMapReady(map: HuaweiMap?) {
        Log.d(TAG, "onMapReady: ")
        this.map = map?.apply {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            setMarkersClustering(false)
        }

        if (!isRestore) {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(59.939095, 30.315868), 10f))
        }

        for (i in 0 until 5) {
        map?.addMarker(
            MarkerOptions()
                .position(LatLng(59.0, 30.0))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .clusterable(true))
        }
    }

    private fun clusterizationOffOn() {
        if (button_clusterization.text.endsWith("off")) {
            map?.setMarkersClustering(true)
            button_clusterization.setTextColor(Color.GREEN)
            button_clusterization.text = resources.getString(R.string.clusterization_on)
        } else if (button_clusterization.text.endsWith("on")) {
            map?.setMarkersClustering(false)
            button_clusterization.setTextColor(Color.parseColor("#FFAAAAAA"))
            button_clusterization.text = resources.getString(R.string.clusterization_off)
        }
    }

    private fun addMarkers() {
        for (i in 0 until 1000) {
            val randomX = (0..100).random()
            val randomY = (0..100).random()
            val x = ("59.$randomX").toDouble()
            val y = ("30.$randomY").toDouble()

            val marker = map?.addMarker(
                MarkerOptions()
                    .position(LatLng(x, y))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    .clusterable(true)
            )
            marker?.let { markers.put(i.toLong(), it) }
        }
    }

    private fun remove50Markers() {
        val markersIterator = markers.iterator()
        var i = 0
        while (markersIterator.hasNext()) {
            if (i > 50) {
                break
            }
            val marker = markersIterator.next()
            marker.value.remove()
            markers.remove(marker.key)
            i++
        }
    }

    private fun removeAllMarkers() {
        val markersIterator = markers.iterator()
        while (markersIterator.hasNext()) {
            val marker = markersIterator.next()
            marker.value.remove()
            markers.remove(marker.key)
        }
    }

    private fun hideMarkers() {
        val markersIterator = markers.iterator()
        while (markersIterator.hasNext()) {
            val marker = markersIterator.next()
            marker.value.isVisible = false
        }
    }

    private fun showMarkers() {
        val markersIterator = markers.iterator()
        while (markersIterator.hasNext()) {
            val marker = markersIterator.next()
            marker.value.isVisible = true
        }
    }

    private fun clearMap() {
        map?.clear()
        markers.clear()
        button_clusterization.setTextColor(Color.parseColor("#FFAAAAAA"))
        button_clusterization.text = resources.getString(R.string.clusterization_off)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_generate_markers -> {
                Log.i(TAG, "onClick: generate markers")
                Log.d(TAG, markers.size.toString())
                if (markers.isEmpty()) {
                    addMarkers()
                }
            }
            R.id.button_remove_markers_big -> {
                Log.i(TAG, "onClick: remove all markers")
                removeAllMarkers()
            }
            R.id.button_remove_50_markers -> {
                Log.i(TAG, "onClick: remove 50 markers")
                remove50Markers()
            }
            R.id.button_clean_map -> {
                Log.i(TAG, "onClick: clean map")
                clearMap()
            }
            R.id.button_clusterization -> {
                Log.i(TAG, "onClick: clusterization")
                clusterizationOffOn()
            }
            R.id.button_hide_markers -> {
                Log.i(TAG, "onClick: hide markers")
                hideMarkers()
            }
            R.id.button_show_markers -> {
                Log.i(TAG, "onClick: show markers")
                showMarkers()
            }
        }
    }
}