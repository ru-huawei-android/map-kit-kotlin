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

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val REQUEST_CODE = 100

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
        }

        button_markers.setOnClickListener(this)
        button_routes.setOnClickListener(this)
        button_big_marker.setOnClickListener(this)
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_markers -> {
                Log.i(TAG, "onClick: markers demo")
                val intent = Intent(this, MarkerActivity::class.java)
                startActivity(intent)
            }
            R.id.button_routes -> {
                Log.i(TAG, "onClick: routes demo")
                val intent = Intent(this, RoutesActivity::class.java)
                startActivity(intent)
            }
            R.id.button_big_marker -> {
                Log.i(TAG, "onClick: big marker demo")
                val intent = Intent(this, BigMarkerActivity::class.java)
                startActivity(intent)
            }
        }
    }
}