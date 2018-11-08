package com.example.lukaszreszetow.mamproject

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdate

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    private var shouldShowRealLocation: Boolean = true
    private val HARDCODED_ETI_POSITION = LatLng(54.370809, 18.613449)
    private val ZOOM_LEVEL = 17f
    private lateinit var mapFragment: SupportMapFragment
    private val locationProvider: String = LocationManager.GPS_PROVIDER
    private lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupButtons()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(locationProvider, 3000, 0f, locationListener)
    }

    private fun setupButtons() {
        mapETIBT.setOnClickListener {
            shouldShowRealLocation = false
            mapFragment.getMapAsync { map ->
                map.clear()
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        HARDCODED_ETI_POSITION,
                        ZOOM_LEVEL
                    )
                )
                map.addMarker(MarkerOptions().position(HARDCODED_ETI_POSITION))
            }
        }
        mapMyPositionBT.setOnClickListener {
            shouldShowRealLocation = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
    }


    private val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            if (shouldShowRealLocation) {
                mapFragment.getMapAsync { map ->
                    map.clear()
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            newLatLng,
                            ZOOM_LEVEL
                        )
                    )
                    map.addMarker(MarkerOptions().position(newLatLng))
                }
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }

    override fun onDestroy() {
        locationManager.removeUpdates(locationListener)
        super.onDestroy()
    }
}
