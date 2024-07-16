package com.haqq.muslim

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.io.IOException
import java.util.Locale

object LocationUtil {
    /**
     * @param context
     * @param lat
     * @param lng
     * @return String of Location(city) Name
     */
    fun getCurrentLocationFromLatLng(
        context: Context,
        lat: Double,
        lng: Double,
    ): String {
        if (Geocoder.isPresent()) {
            try {
                val gc = Geocoder(context, Locale.getDefault())
                val addresses = gc.getFromLocation(lat, lng, 1)

                addresses?.let {
                    return when {
                        addresses.first().subAdminArea != null -> addresses.first().subAdminArea
                        addresses.first().adminArea != null -> addresses.first().adminArea
                        addresses.first().subLocality != null -> addresses.first().subLocality
                        addresses.first().locality != null -> addresses.first().locality
                        else -> ""
                    }
                }
            } catch (e: IOException) {
                e.let {
                    Log.e(context.toString(), e.toString())
                }
            }
        }
        return "Depok"
    }
}
