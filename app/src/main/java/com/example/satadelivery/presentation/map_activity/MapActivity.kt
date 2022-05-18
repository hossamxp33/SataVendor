package com.example.satadelivery.presentation.map_activity


import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.satadelivery.R
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.helper.MapHelper
import com.example.satadelivery.helper.PreferenceHelper

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.map_activity.*
import java.io.IOException
import java.util.*
import javax.inject.Inject





class MapActivity : AppCompatActivity() , HasAndroidInjector, OnMapReadyCallback {

    @Inject
    lateinit var Pref: PreferenceHelper
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var map: GoogleMap
    var latitude: Double? = null //-33.867
    var longitude: Double? = null // 151.206
    val overlaySize = 100f
    var address = ""
    var streetName:String? = null
    var place_id = ""
  //   var branchesList = ArrayList<BranchesModelListItem>()
    var addresses: List<Address>? = null

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//
//
//    val viewModel by  viewModels<MapViewModel> { viewModelFactory }

    public override fun onCreate(icicle: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(icicle)
        BaseApplication.appComponent.inject(this)

        setContentView(R.layout.map_activity)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


        menu.setOnClickListener {
            ClickHandler().OpenMyFragment(this)
        }


    }


    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.clear();
        MapHelper().setPoiClick(map)
        MapHelper().setMapStyle(map, this)
        getLocationPermission()

        if (MapHelper().CheckPermission(this))
            if (MapHelper().isLocationEnabled(this)) {
                enableMyLocation(this)
            } else {

                Toast.makeText(
                    this,
                    "Please Turn on Your device Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        else {

            MapHelper().RequestPermission(this)

        }

    }
    @SuppressLint("MissingPermission")
    fun enableMyLocation(context: Context) {
        map.isMyLocationEnabled = true
        mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            latitude = location?.latitude!!
            longitude = location.longitude!!
            if (location == null) {
                MapHelper().NewLocationData(context)
            } else {
                val homeLatLng = LatLng(latitude!!, longitude!!)
                val zoomLevel = 15f
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
                map.addMarker(MarkerOptions().position(homeLatLng))
             //   setMapLongClick(map)
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                val googleOverlay = GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
                    .position(homeLatLng, overlaySize)
                map.addGroundOverlay(googleOverlay)

            }

        }

    }
    @SuppressLint("MissingPermission")
    private fun getLocationPermission() {
        if (MapHelper().CheckPermission(this)
        ) {
            mFusedLocationClient?.lastLocation?.addOnSuccessListener(this,
                OnSuccessListener<Location?> { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        latitude = location.latitude
                        longitude = location.longitude
                        goToAddress(latitude!!, longitude!!)

                    }
                })
        } else {
            MapHelper().RequestPermission(this)
        }
    }

    private fun goToAddress(mlatitude: Double, mLogitude: Double) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mlatitude, mLogitude), 16.0f))
        map.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
            latitude = map.cameraPosition.target.latitude
            longitude = map.cameraPosition.target.longitude
            address = getLocationAddress().toString()

        })
    }

    fun getLocationAddress(): String? {
        val gcd = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = gcd.getFromLocation(latitude!!, longitude!!, 1)

            if (addresses!=null && addresses.isNotEmpty()) {
                val locality = addresses[0].getAddressLine(0)
                val country = addresses[0].countryName
                val state = addresses[0].adminArea
                val sub_admin = addresses[0].subAdminArea
                val city = addresses[0].featureName
                val pincode = addresses[0].postalCode
                val locality_city = addresses[0].locality
                val sub_localoty = addresses[0].subLocality
                val streetName = addresses[0].thoroughfare

                if (locality != null) {
               //     deliveryLocation.setText((if (streetName != null) "$streetName, " else "") + (if (locality_city != null) locality_city.toString() + ", " else "") + (if (sub_admin != null) sub_admin.toString() else "")  )




                }
                } else {


                }


        } catch (e: IOException) {
            e.printStackTrace()
        }

//        try {
//            val listAddresses = geocoder.getFromLocation(
//                Lat!!, Lng!!, 1
//            )
//            if (null != listAddresses && !listAddresses.isEmpty()) {
//                val address = StringBuilder()
//                if (listAddresses[0].maxAddressLineIndex > 0) {
//
//
//                    for (i in 0 until listAddresses[0].maxAddressLineIndex) {
//                        address.append(listAddresses[0].getAddressLine(i)).append(", ")
//                    }
//                } else {
//                    address.append(listAddresses[0].getAddressLine(0))
//                }
//                return address.toString()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        return null
    }

}