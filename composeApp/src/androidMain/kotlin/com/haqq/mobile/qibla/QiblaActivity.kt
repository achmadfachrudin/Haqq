package com.haqq.mobile.qibla

import AppConstant.DEFAULT_LOCATION_LATITUDE
import AppConstant.DEFAULT_LOCATION_LONGITUDE
import AppConstant.DEFAULT_LOCATION_NAME
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.haqq.mobile.databinding.ActivityQiblaBinding
import com.haqq.mobile.showToast
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import org.koin.android.ext.android.inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class QiblaActivity :
    AppCompatActivity(),
    SensorEventListener {
    private var currentAzimuth: Float = 0.toFloat()

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val rFloatArray = FloatArray(9)
    private val iFloatArray = FloatArray(9)

    private val samplingPeriod = SensorManager.SENSOR_DELAY_GAME

    private var qiblaDegree: Int = 0
    private var qiblaMinDegree: Int = 0
    private var qiblaMaxDegree: Int = 0

    private var locationLatitude = DEFAULT_LOCATION_LATITUDE
    private var locationLongitude = DEFAULT_LOCATION_LONGITUDE
    private var locationName = DEFAULT_LOCATION_NAME

    private lateinit var language: AppSetting.Language

    private val appRepository: AppRepository by inject()

    private lateinit var binding: ActivityQiblaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureBinding()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        getSetting()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer != null && magnetometer != null) {
            getQiblaDirection(
                latitude = locationLatitude,
                longitude = locationLongitude,
            )

            setupCompass()
        } else {
            showToast(AppString.DEVICE_NOT_SUPPORTED.getString())
        }
    }

    private fun configureBinding() {
        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()

        accelerometer?.also { acc ->
            sensorManager?.registerListener(this, acc, samplingPeriod)
        }
        magnetometer?.also { mag ->
            sensorManager?.registerListener(this, mag, samplingPeriod)
        }
    }

    private fun getSetting() {
        val setting = appRepository.getSetting()

        locationName = setting.location.name
        locationLatitude = setting.location.latitude
        locationLongitude = setting.location.longitude

        language = setting.language
    }

    /**
     * ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
     */
    private fun getQiblaDirection(
        latitude: Double,
        longitude: Double,
    ) {
        val kabahLongitude = 39.826206
        val kabahLatitude = 21.422487

        val longitudeDifference = Math.toRadians(kabahLongitude - longitude)
        val latitudeBekasi = Math.toRadians(latitude)
        val latitudeKabah = Math.toRadians(kabahLatitude)

        val x = sin(longitudeDifference) * cos(latitudeKabah)
        val y =
            cos(latitudeBekasi) * sin(latitudeKabah) - sin(latitudeBekasi) *
                cos(
                    latitudeKabah,
                ) * cos(longitudeDifference)

        val bearing = atan2(x, y)
        val bearingDegrees = Math.toDegrees(bearing)

        val degreeResult = (bearingDegrees + 360) % 360

        qiblaDegree = degreeResult.toInt()
        qiblaMinDegree = qiblaDegree - 5
        qiblaMaxDegree = qiblaDegree + 5
    }

    private fun setupCompass() {
        val title = "${AppString.QIBLA_DEGREE.getString()} $qiblaDegree°"
        binding.toolbar.title = title
        binding.tvLocation.text = locationName
        binding.tvLatitude.text = "$locationLatitude"
        binding.tvLongitude.text = "$locationLongitude"

        binding.tvDegree.text = "$qiblaDegree"
        binding.tvLocation.text = locationName
        binding.tvNote.text = AppString.QIBLA_NOTE.getString()
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f

        synchronized(this) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
            }

            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
            }

            val success =
                SensorManager.getRotationMatrix(rFloatArray, iFloatArray, mGravity, mGeomagnetic)

            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rFloatArray, orientation)
                var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + 360) % 360

                binding.tvAzimuth.text = "${azimuth.toInt()}"
                val status =
                    if (azimuth.toInt() in qiblaMinDegree..qiblaMaxDegree) {
                        AppString.QIBLA_STATUS_CORRECT.getString()
                    } else {
                        AppString.QIBLA_STATUS_INCORRECT.getString()
                    }

                binding.tvStatus.text = status

                adjustDialImage(azimuth)
                adjustArrowQibla(azimuth)
            }
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int,
    ) {
    }

    private fun adjustDialImage(azimuth: Float) {
        val an =
            RotateAnimation(
                -currentAzimuth,
                -azimuth,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
            )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.imgCompass.startAnimation(an)
    }

    private fun adjustArrowQibla(azimuth: Float) {
        val an =
            RotateAnimation(
                -currentAzimuth + qiblaDegree.toFloat(),
                -azimuth,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
            )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.imgQibla.startAnimation(an)
    }
}
