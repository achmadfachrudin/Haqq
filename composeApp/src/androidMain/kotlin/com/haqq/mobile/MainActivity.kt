package com.haqq.mobile

import App
import AppConstant.DEFAULT_LOCATION_LATITUDE
import AppConstant.DEFAULT_LOCATION_LONGITUDE
import AppConstant.DEFAULT_LOCATION_NAME
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import getPlatform
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 111
        const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000L
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest =
        LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS)
            .build()

    private val locationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                try {
                    setLocation(locationResult.locations.last())
                    Log.d("Location", "Set Location ${locationResult.locations.last()}")
                } catch (e: Exception) {
                    showToast("Error Location Callback: ${e.message}")
                    Log.e("Location", "Error: ${e.message}")
                    showScreen()
                }
            }
        }

    var locationLatitude = DEFAULT_LOCATION_LATITUDE
    var locationLongitude = DEFAULT_LOCATION_LONGITUDE
    var locationName = DEFAULT_LOCATION_NAME

    val appRepository: AppRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        val bundle =
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SOURCE_PLATFORM, getPlatform().name)
            }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    private fun trackScreen() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, AnalyticsConstant.HOME_SCREEN)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                PERMISSION_REQUEST_CODE,
            )
            return
        }

        if (isGPSEnabled()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper(),
            )
        } else {
            showScreen()
        }

        Log.d("Location", "isGPSEnabled() ${isGPSEnabled()}")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                showToast("Without location access, we can't provide personalized prayer times. You can enable anytime")
                showScreen()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setLocation(location: Location) {
        val newLocationName =
            LocationUtil.getCurrentLocationFromLatLng(
                this,
                location.latitude,
                location.longitude,
            )

        if (location.latitude != locationLatitude &&
            location.latitude != DEFAULT_LOCATION_LATITUDE &&
            location.longitude != locationLongitude &&
            location.longitude != DEFAULT_LOCATION_LONGITUDE &&
            newLocationName != locationName
        ) {
            locationLatitude = location.latitude
            locationLongitude = location.longitude
            locationName = newLocationName

            val currentLocation = appRepository.getSetting().location
            if (currentLocation.name != newLocationName) {
                appRepository.updateLocation(
                    AppSetting.Location(
                        latitude = locationLatitude,
                        longitude = locationLongitude,
                        name = locationName,
                    ),
                )
            }
        }

        showScreen()
    }

    private fun showScreen() {
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

        Log.d(
            "Location",
            "location: $locationName, Latitude: $locationLatitude, Longitude: $locationLongitude",
        )

        trackScreen()

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
