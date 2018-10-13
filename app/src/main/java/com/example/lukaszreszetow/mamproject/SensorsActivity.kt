package com.example.lukaszreszetow.mamproject

import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sensors.*

class SensorsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensors)
        setupGyro()
        setupMagn()
        setupAcc()
    }

    private fun setupAcc() {
        ReactiveSensors(this).observeSensor(Sensor.TYPE_ACCELEROMETER)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterSensorChanged())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val event = it.sensorEvent
                    accX.text = String.format("%.3f", event.values[0])
                    accY.text = String.format("%.3f", event.values[1])
                    accZ.text = String.format("%.3f", event.values[2])
                    if(event.values[0] > 6){
                        finish()
                    }
                }
    }

    private fun setupMagn() {
        ReactiveSensors(this).observeSensor(Sensor.TYPE_MAGNETIC_FIELD)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterSensorChanged())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val event = it.sensorEvent
                    magnX.text = String.format("%.3f", event.values[0])
                    magnY.text = String.format("%.3f", event.values[1])
                    magnZ.text = String.format("%.3f", event.values[2])
                }
    }

    private fun setupGyro() {
        ReactiveSensors(this).observeSensor(Sensor.TYPE_GYROSCOPE)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterSensorChanged())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val event = it.sensorEvent
                    gyroX.text = String.format("%.3f", event.values[0])
                    gyroY.text = String.format("%.3f", event.values[1])
                    gyroZ.text = String.format("%.3f", event.values[2])
                }
    }
}
