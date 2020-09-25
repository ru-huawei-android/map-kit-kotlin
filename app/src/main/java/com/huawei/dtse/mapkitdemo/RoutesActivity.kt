package com.huawei.dtse.mapkitdemo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huawei.dtse.mapkitdemo.direction.utils.requestBody
import com.huawei.dtse.mapkitdemo.direction.viewmodel.DirectionViewModel
import com.huawei.dtse.mapkitdemo.utils.append
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import kotlinx.android.synthetic.main.activity_marker.mapView
import kotlinx.android.synthetic.main.activity_routes.*


private const val TAG = "RoutesActivity"
private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

class RoutesActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    //HUAWEI map
    private var map: HuaweiMap? = null

    //List for markers
    private var markerList: MutableList<Marker> = ArrayList()

    private var isRestore = false

    private var directionViewModel: DirectionViewModel? = null

    private var polylineList: MutableList<Polyline> = ArrayList()

    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        isRestore = savedInstanceState != null

        directionViewModel = ViewModelProvider(this).get(DirectionViewModel::class.java)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapView.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@RoutesActivity)
        }

        button_driving_route.setOnClickListener(this)
        button_walking_route.setOnClickListener(this)
        button_bicycling_route.setOnClickListener(this)
        button_remove_all.setOnClickListener(this)
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
        this.map = map
        this.map?.let {
            addMapListener()
            addPolylineListener()
        }

        if (!isRestore) {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(59.939095, 30.315868), 10f))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_bicycling_route -> {
                Log.i(TAG, "onClick: button_bicycling_route")
                if (markerList.size == 2) {
                    removeAllPolylines()
                    makeBicyclingRoute()
                }
            }
            R.id.button_driving_route -> {
                Log.i(TAG, "onClick: button_driving_route")
                if (markerList.size == 2) {
                    removeAllPolylines()
                    makeDrivingRoute()
                }
            }
            R.id.button_walking_route -> {
                Log.i(TAG, "onClick: button_walking_route")
                if (markerList.size == 2) {
                    removeAllPolylines()
                    makeWalkingRoute()
                }
            }
            R.id.button_remove_all -> {
                Log.i(TAG, "onClick: button_remove_all")
                start_text.apply {
                    start_text.text = text(context, R.string.start_text)
                    setTextColor(Color.RED)
                }
                removeAllPolylines()
                removeAllMarkers()
            }
        }
    }

    private fun makeDrivingRoute() {
        var avoid: Array<Int?> = arrayOf()
        if (avoid_toll_roads.isChecked) avoid = append(avoid, 1)
        if (avoid_expressways.isChecked) avoid = append(avoid, 2)

        directionViewModel?.let {
            requestBody(
                markerList[0].position,
                markerList[1].position,
                alternatives.isChecked,
                avoid
            ).let { it1 ->
                progress_bar.visibility = View.VISIBLE
                it.getDrivingDirectionLiveData(it1)
                    .observe(this, Observer { direction ->
                        if (direction.returnDesc != "OK") {
                            Toast.makeText(this, "Cannot make the route.", Toast.LENGTH_SHORT)
                                .show()
                            return@Observer
                        }
                        Log.d(TAG, direction.toString())
                        it.getPolylineLiveData(direction)
                            .observe(this, Observer { routes ->
                                start_text.apply {
                                    text = text(context, R.string.last_step_text)
                                    setTextColor(Color.BLUE)
                                }

                                routes.forEach {
                                    val polyline = map?.addPolyline(
                                        PolylineOptions()
                                            .addAll(it.value)
                                            .clickable(true)
                                            .color(if (alternatives.isChecked) Color.GRAY else Color.BLUE)
                                            .width(3f)
                                    )
                                    polyline?.let { it1 ->
                                        polylineList.add(it1)
                                    }
                                }
                                progress_bar.visibility = View.GONE
                            })
                    })
            }
        }
    }

    private fun makeWalkingRoute() {
        directionViewModel?.let {
            requestBody(
                markerList[0].position,
                markerList[1].position
            ).let { it1 ->
                progress_bar.visibility = View.VISIBLE
                it.getWalkingDirectionLiveData(it1)
                    .observe(this, Observer { direction ->
                        if (direction.returnDesc != "OK") {
                            Toast.makeText(this, "Cannot make the route.", Toast.LENGTH_SHORT)
                                .show()
                            return@Observer
                        }
                        Log.d(TAG, direction.toString())
                        it.getPolylineLiveData(direction)
                            .observe(this, Observer { routes ->
                                start_text.apply {
                                    text = text(context, R.string.last_step_text)
                                    setTextColor(Color.BLUE)
                                }
                                routes.forEach {
                                    val polyline = map?.addPolyline(
                                        PolylineOptions()
                                            .addAll(it.value)
                                            .color(Color.GREEN)
                                            .width(3f)
                                    )
                                    polyline?.let { it1 -> polylineList.add(it1) }
                                }
                                progress_bar.visibility = View.GONE
                            })
                    })
            }
        }
    }

    private fun makeBicyclingRoute() {
        directionViewModel?.let {
            requestBody(
                markerList[0].position,
                markerList[1].position
            ).let { it1 ->
                progress_bar.visibility = View.VISIBLE
                it.getBicyclingDirectionLiveData(it1)
                    .observe(this, Observer { direction ->
                        if (direction.returnDesc != "OK") {
                            Toast.makeText(this, "Cannot make the route.", Toast.LENGTH_SHORT)
                                .show()
                            return@Observer
                        }
                        Log.d(TAG, direction.toString())
                        it.getPolylineLiveData(direction)
                            .observe(this, Observer { routes ->
                                start_text.apply {
                                    text = text(context, R.string.last_step_text)
                                    setTextColor(Color.BLUE)
                                }
                                routes.forEach {
                                    val polyline = map?.addPolyline(
                                        PolylineOptions()
                                            .addAll(it.value)
                                            .color(Color.RED)
                                            .width(3f)
                                    )
                                    polyline?.let { it1 -> polylineList.add(it1) }
                                }
                                progress_bar.visibility = View.GONE
                            })
                    })
            }
        }
    }

    /**
     * Set the listener associated with a polyline
     */
    private fun addPolylineListener() {
        map?.setOnPolylineClickListener {
            Log.d(TAG, "Polyline is clicked")
            for (polyline in polylineList) {
                polyline.color = Color.GRAY
                polyline.width = 1f
            }
            it.color = Color.BLUE
            it.width = 3f
        }
    }

    /**
     * Set the listener associated with the map
     */
    private fun addMapListener() {

        map?.setOnMapLongClickListener {
            if (counter == 0) {
                removeAllMarkers()
                removeAllPolylines()
                addMarker(it, "Start")
                start_text.apply {
                    start_text.text = text(context, R.string.second_step_text)
                    setTextColor(Color.RED)
                }
                counter++
            } else {
                addMarker(it, "Finish")
                start_text.apply {
                    start_text.text = text(context, R.string.third_step_text)
                    setTextColor(Color.RED)
                }
                counter--
            }
        }
    }

    private fun addMarker(latLng: LatLng, title: String) {
        val iconColors = arrayOf(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )

        val marker = map?.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(iconColors[counter])
                .title(title)
        )
        marker?.let { markerList.add(it) }
    }

    private fun removeAllMarkers() {
        for (marker in markerList) marker.remove()
        markerList.clear()
    }

    private fun removeAllPolylines() {
        for (polyline in polylineList) polyline.remove()
        polylineList.clear()
    }

    private fun text(context: Context, text: Int): CharSequence {
        return context.getText(text)
    }
}


