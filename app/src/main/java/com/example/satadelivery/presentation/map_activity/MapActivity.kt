package com.example.satadelivery.presentation.map_activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.satadelivery.R
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.helper.MapHelper
import com.example.satadelivery.helper.PreferenceHelper
import com.example.satadelivery.presentation.new_order_bottomfragment.NewOrderFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.map_activity.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException
import java.util.*
import javax.inject.Inject
import com.beust.klaxon.*
import com.example.satadelivery.models.current_orders.OrderDetail
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.Permissions
import com.example.satadelivery.presentation.auth.LoginActivity
import org.jetbrains.anko.custom.onUiThread

import org.jetbrains.anko.custom.async
import java.net.URL
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.example.satadelivery.presentation.current_order_fragment.CurrentOrderFragment
import com.example.satadelivery.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainIntent
import com.example.satadelivery.presentation.details_order_fragment.DetailsOrderFragment
import com.example.satadelivery.presentation.history_order_fragment.DailyOrdersFragment
import com.example.satadelivery.presentation.history_order_fragment.HistoryOrderFragment
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.custom.async

class MapActivity : AppCompatActivity(), HasAndroidInjector, OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var Pref: PreferenceHelper
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var map: GoogleMap
    var latitude: Double? = null //-33.867
    var longitude: Double? = null // 151.206

    val overlaySize = 100f
    var address = ""

    var mDrawerLayout: DrawerLayout? = null

    var homeLatLng = LatLng(0.0, 0.0)

    //   var branchesList = ArrayList<BranchesModelListItem>()
    var intent1: Intent? = null

     var mSocket: Socket? = null

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var distance: Double? = null

    var locationRequest: LocationRequest? = null

    val viewModel by viewModels<CurrentOrderViewModel> { viewModelFactory }

    public override fun onCreate(icicle: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(icicle)
        BaseApplication.appComponent.inject(this)

        setContentView(R.layout.map_activity)
        mDrawerLayout = findViewById(R.id.drawerLayout)


        val app: BaseApplication = application as BaseApplication

        mSocket = app.getMSocket()
            //connecting socket
        mSocket?.connect()
        mSocket?.emit("CreateDeliveryRoom", Pref.room_id!!)

        // mSocket?.emit("makeNewOrder",122)
        val options = IO.Options()
        options.reconnection = true //reconnection
        options.forceNew = true


        mSocket?.on("RetriveDeliveryOrder") {

            var mp = MediaPlayer.create(this, R.raw.alarm);

            mp.start();

            runOnUiThread {

                val gson = Gson()

                var json = it.first().toString()

                val type = object : TypeToken<OrdersItem?>() {}.type

                var newitem = gson.fromJson<OrdersItem>(json, type)

                ClickHandler().openDialogFragment(this, NewOrderFragment(newitem!!),"")

             //   Log.d("socket", json.toString())

            }

        }

        nav_view.setNavigationItemSelectedListener(this)



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)

        siteDrawerMenuButton.setOnClickListener { view ->
            this.openCloseNavigationDrawer(view)
            note.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.note));

        }

        note.setOnClickListener {
            note.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.note_active));
            ClickHandler().openDialogFragment(this, CurrentOrderFragment(viewModel), "")
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
        //MapHelper().setMapStyle(map, this)
        statusCheck()


        getLocationPermission()
        // Add a marker in Sydney and move the camera
        getClientAddress()


        //  map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0), 16.0f))

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
             if (location == null) {

            MapHelper().NewLocationData(context)

        }
            if (Pref.latitude != "" && Pref.longitude != "") {

                latitude = Pref.latitude?.toDouble()

                longitude = Pref.longitude?.toDouble()

                homeLatLng = LatLng(latitude!!, longitude!!)

            } else {

                latitude = location!!.latitude

                longitude = location.longitude

                homeLatLng = LatLng(latitude!!, longitude!!)

                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                val googleOverlay = GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
                    .position(homeLatLng, overlaySize)
                map.addGroundOverlay(googleOverlay)

            }


        }

    }

        fun updateLocation(){
        //Instantiating the Location request and setting the priority and the interval I need to update the location.
        locationRequest = LocationRequest.create();
        locationRequest?.setInterval(100);
        locationRequest?.setFastestInterval(50);
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //instantiating the LocationCallBack
        //instantiating the LocationCallBack
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult != null) {
                    if (locationResult == null) {
                        return
                    }
                    //Showing the latitude, longitude and accuracy on the home screen.
                    //      for (location in locationResult.locations) {
                    // map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng( locationResult.lastLocation.latitude, locationResult.lastLocation.longitude), 16.0f))
                    latitude = locationResult.lastLocation.latitude
                    longitude = locationResult.lastLocation.longitude


                    //   }
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationClient?.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())

    }

    @SuppressLint("MissingPermission")
    private fun getLocationPermission() {
        if (MapHelper().CheckPermission(this)) {
            mFusedLocationClient?.lastLocation?.addOnSuccessListener(this,
                OnSuccessListener<Location?> { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        if (Pref.latitude != "" && Pref.longitude != "") {
                            // Logic to handle location object
                            latitude = Pref.latitude?.toDouble()
                            longitude = Pref.longitude?.toDouble()
                            goToAddress(latitude!!, longitude!!)

                        } else {
                            latitude = location.latitude
                            longitude = location.longitude
                            goToAddress(latitude!!, longitude!!)
                        }
                    }
                    Pref.latitude = latitude.toString()
                    Pref.longitude = longitude.toString()
                })
        } else {
            MapHelper().RequestPermission(this)
        }
    }

    private fun goToAddress(mlatitude: Double, mLogitude: Double) {
        val homeLatLng = LatLng(mlatitude, mLogitude)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mlatitude, mLogitude), 16.0f))
        map.addMarker(MarkerOptions().position(homeLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_delivery)))
        map.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
            latitude = map.cameraPosition.target.latitude
            longitude = map.cameraPosition.target.longitude
        })
    }


    fun getClientAddress() {
        try {
            lifecycleScope.launchWhenStarted {
                viewModel.state.collect {
                    if (it != null) {
                        val end_latitude = it.cliendLatitude
                        val end_longitude = it.cliendLongitude

                        if (end_latitude != null && end_longitude != null)
                            if (it.progress == true) {
                                val clientLatLng = LatLng(end_latitude, end_longitude)
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                                    end_latitude,
                                    end_longitude), 16.0f))

                                map.addMarker(MarkerOptions().position(clientLatLng))

                                val options = PolylineOptions()
                                options.color(this@MapActivity.getColor(R.color.orange))
                                options.width(10f)
                                val url = getURL(homeLatLng, clientLatLng)

                                    async {
                                        // Connect to URL, download content and convert into string asynchronously
                                        val result = URL(url).readText()
                                        val LatLongB = LatLngBounds.Builder()

                                        onUiThread {
                                            // When API call is done, create parser and convert into JsonObjec
                                            val parser: Parser = Parser()
                                            val stringBuilder: StringBuilder = StringBuilder(result)
                                            val json: com.beust.klaxon.JsonObject =
                                                parser.parse(stringBuilder) as com.beust.klaxon.JsonObject
                                            // get to the correct element in JsonObject
                                            try {

                                                val routes =
                                                    json.array<com.beust.klaxon.JsonObject>("routes")

                                                val points =
                                                    routes!!["legs"]["steps"][0] as com.beust.klaxon.JsonArray<com.beust.klaxon.JsonObject>

                                                // For every element in the JsonArray, decode the polyline string and pass all points to a List

                                                val polypts =
                                                    points.flatMap {
                                                        decodePoly(it.obj("polyline")
                                                            ?.string("points")!!)
                                                    }
                                                // Add  points to polyline and bounds

                                                options.add(homeLatLng)
                                                LatLongB.include(homeLatLng)
                                                for (point in polypts) {
                                                    options.add(point)
                                                    LatLongB.include(point)
                                                }
                                                options.add(clientLatLng)
                                                LatLongB.include(clientLatLng)
                                                // build bounds
                                                val bounds = LatLongB.build()
                                                // add polyline to the map
                                                map.addPolyline(options)
                                                // show map with route centered
                                                map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                                                    bounds,
                                                    100))
                                            } catch (e: Exception) {
                                            }
                                        }
                                    }


                            }

                    } else
                        Toast.makeText(
                            this@MapActivity,
                            "Please Turn on Your device Location",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params&key=AIzaSyCjzzd4nbOiZJx3B53u9ZZAq0tcOsVUBdg"

    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.current_orders -> {
                // Handle the camera action
                ClickHandler().openDialogCurrentOrderFragment(this,
                    CurrentOrderFragment(viewModel),
                    CurrentOrderFragment.TAG, viewModel)
            }
            R.id.dailyOrder -> {
                ClickHandler().openDialogFragment(this,
                    DailyOrdersFragment(),
                    DailyOrdersFragment.TAG)
            }
            R.id.archiveOrders -> {
                ClickHandler().openDialogFragment(this,
                    HistoryOrderFragment(),
                    HistoryOrderFragment.TAG)
            }
//            R.id.nav_tools -> {
//
//            }
            R.id.logout -> {
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }

        mDrawerLayout?.closeDrawer(GravityCompat.END)
        return true
    }

    fun openCloseNavigationDrawer(view: View) {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.END)
        } else {
            mDrawerLayout!!.openDrawer(GravityCompat.END)
        }
    }


    fun statusCheck() {
        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        gpsStatus()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        dialog.cancel()
    }

    fun gpsStatus() {
        intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent1);
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
        builder.setPositiveButton(android.R.string.yes, positiveButtonClick)
        builder.setNegativeButton(android.R.string.no, negativeButtonClick)
        val alert: AlertDialog = builder.create()
        alert.show()
    }


    override fun onResume() {
        super.onResume()
        getLocationPermission()
        updateLocation()

    }


}