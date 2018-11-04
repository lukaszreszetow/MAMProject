package com.example.lukaszreszetow.mamproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
    }

}
