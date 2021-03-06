package com.example.lukaszreszetow.mamproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtons()
        if (OpenCVLoader.initDebug()) {
            Toast.makeText(this, "OpenCv zaladowane", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(this, "OpenCv ERROR", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupButtons() {
        sensorsBT.setOnClickListener {
            startActivity(Intent(this, SensorsActivity::class.java))
        }

        colorRecognitionBT.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSION_CAMERA_COLOR
                )
            } else {

                startActivity(Intent(this, ColorRecognitionActivity::class.java))

            }
        }

        mapBT.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_LOCATION
                )
            } else {
                startActivity(Intent(this, MapActivity::class.java))
            }
        }

        VrBT.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSION_CAMERA_VR
                )
            } else {
                startActivity(Intent(this, VrActivity::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CAMERA_COLOR -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(Intent(this, ColorRecognitionActivity::class.java))
            }
            MY_PERMISSION_CAMERA_VR -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(Intent(this, VrActivity::class.java))
            }
            MY_PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(Intent(this, MapActivity::class.java))
            }
        }
    }

    companion object {
        const val MY_PERMISSION_CAMERA_COLOR = 123
        const val MY_PERMISSION_CAMERA_VR = 125
        const val MY_PERMISSION_LOCATION = 124
    }
}


