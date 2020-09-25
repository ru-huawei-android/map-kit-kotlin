package com.huawei.dtse.mapkitdemo.direction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.huawei.dtse.mapkitdemo.direction.model.Direction
import com.huawei.dtse.mapkitdemo.direction.repository.DirectionRepository
import com.huawei.hms.maps.model.LatLng
import okhttp3.RequestBody

class DirectionViewModel: ViewModel() {

    private val directionRepository = DirectionRepository()

    fun getDrivingDirectionLiveData(request: RequestBody): LiveData<Direction> {
        return directionRepository.getDrivingDirection(request)
    }

    fun getWalkingDirectionLiveData(request: RequestBody): LiveData<Direction> {
        return directionRepository.getWalkingDirection(request)
    }

    fun getBicyclingDirectionLiveData(request: RequestBody): LiveData<Direction> {
        return directionRepository.getBicyclingDirection(request)
    }

    fun getPolylineLiveData(direction: Direction): LiveData<MutableMap<Int, List<LatLng>>> {
        return directionRepository.getPolyline(direction)
    }
}