package com.example.satadelivery.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.satadelivery.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapHelper  {
    internal lateinit var mLocationCallback: LocationCallback
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mapReady = false
    var latitude: Double? = null //-33.867
    var longitude: Double? = null // 151.206
    val overlaySize = 100f
    val PERMISSION_ID = 1010

    private val TAG = this::class.java.simpleName
    private lateinit var map: GoogleMap

    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationResult: LocationRequest
    private val REQUEST_LOCATION_PERMISSION = 1
    fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker?.showInfoWindow()
        }
    }




    // Allows map styling and theming to be customized.
     fun setMapStyle(map: GoogleMap , context: Context) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .snippet(snippet)

            )
        }
    }

     fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
          context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
     }
    fun isLocationEnabled(context: Context):Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)


    }
    fun CheckPermission(context: Context):Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        ){
            return true
        }

        return false

    }

    fun RequestPermission(context: Context){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    fun NewLocationData(context: Context) {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

//    fun searchLocation() {
//        val locationSearch: EditText? = null
//        lateinit var location: String
//        location = locationSearch!!.text.toString()
//        var addressList: List<Address>? = null
//
//        if (location == null || location == "") {
//            Toast.makeText(requireContext(),"provide location", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            val geoCoder = Geocoder(requireContext())
//            try {
//                addressList = geoCoder.getFromLocationName(location, 1)
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            val address = addressList!![0]
//            val latLng = LatLng(address.latitude, address.longitude)
//            map.addMarker(MarkerOptions().position(latLng).title(location))
//            map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//            Toast.makeText(requireContext(), address.latitude.toString() + " " + address.longitude, Toast.LENGTH_LONG).show()
//        }
//    }


}