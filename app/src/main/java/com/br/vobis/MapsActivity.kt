package com.br.vobis

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.br.vobis.helper.Permissoes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        Permissoes.validarPermissoes(permissions, this, 1)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val actualLocation = LatLng(location.latitude, location.longitude)

                Toast.makeText(this@MapsActivity, getString(R.string.select_position), Toast.LENGTH_SHORT).show()

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actualLocation, 18f))
                mMap.setOnMapClickListener { latLng ->

                    mMap.clear()

                    mMap.addMarker(MarkerOptions()
                            .position(latLng))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                    btn_confirm_location.visibility = View.VISIBLE

                    btn_confirm_location.setOnClickListener {
                        val intent = Intent()

                        intent.putExtra("lat", latLng.latitude)
                        intent.putExtra("long", latLng.longitude)

                        setResult(AppCompatActivity.RESULT_OK, intent)

                        finish()
                    }
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                return
            }

            override fun onProviderEnabled(provider: String) {
                return

            }

            override fun onProviderDisabled(provider: String) {
                return

            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, locationListener)
            mMap.isMyLocationEnabled = true
        }

        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (resultPermission in grantResults) {
            val permissionDenied = resultPermission == PackageManager.PERMISSION_DENIED

            if (permissionDenied) {
                val msg = getString(R.string.require_permissions)
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, locationListener)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        locationManager.removeUpdates(locationListener)
    }
}
