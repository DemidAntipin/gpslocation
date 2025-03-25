package com.example.gpslocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {
    val LOCATION_PERM_CODE = 2
    lateinit var icon : ImageView;
    var isTracking = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        icon = findViewById(R.id.icon)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // запрашиваем разрешения на доступ к геопозиции
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // переход в запрос разрешений
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERM_CODE)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        isTracking=true
        val prv = locationManager.getBestProvider(Criteria(), true)
        Log.d("my", locationManager.allProviders.toString())
        if (prv != null) {
            icon.setImageResource(R.drawable.gps);
            val location = locationManager.getLastKnownLocation(prv)
            if (location != null) {
                displayCoord(location.latitude, location.longitude)
            }
            Log.d("mytag", "location set")
        }
        else {
            icon.setImageResource(R.drawable.nogps);
        }
        val track = findViewById<Button>(R.id.trackButton)
        track.setOnClickListener {
            if (!isTracking){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                val prv = locationManager.getBestProvider(Criteria(), true)
                if (prv != null) {
                    icon.setImageResource(R.drawable.gps);
                    val location = locationManager.getLastKnownLocation(prv)
                    if (location != null) {
                        displayCoord(location.latitude, location.longitude)
                    }
                    Log.d("mytag", "location set")
                }
                else {
                    icon.setImageResource(R.drawable.nogps);
                }
                isTracking=true
            }
        }
        val donttrack = findViewById<Button>(R.id.notrackButton)
        donttrack.setOnClickListener {
            if (isTracking){
                locationManager.removeUpdates(this)
                icon.setImageResource(R.drawable.nogps)
                isTracking=false
            }
        }
    }

    override fun onLocationChanged(loc: Location) {
        val lat = loc.latitude
        val lng = loc.longitude
        displayCoord(lat, lng)
        Log.d("my", "lat " + lat + " long " + lng)
    }

    fun displayCoord(latitude: Double, longitude: Double) {
        findViewById<TextView>(R.id.lat).text = String.format("%.5f", latitude)
        findViewById<TextView>(R.id.lng).text = String.format("%.5f", longitude)
    }

    // TODO: обработать случай отключения GPS (геолокации) пользователем
    // onProviderDisabled + onProviderEnabled
    override fun onProviderEnabled(provider: String) {
        icon.setImageResource(R.drawable.gps);
    }
    override fun onProviderDisabled(provider: String) {
        icon.setImageResource(R.drawable.nogps);
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    // TODO: обработать возврат в активность onRequestPermissionsResult
}