package com.codesroots.satavendor.presentation.map_activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
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
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.beust.klaxon.Parser
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.MapActivityBinding
import com.codesroots.satavendor.databinding.NavHeaderMainBinding
import com.codesroots.satavendor.helper.*
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.presentation.auth.LoginActivity
import com.codesroots.satavendor.presentation.current_order_fragment.CurrentOrderFragment
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.codesroots.satavendor.presentation.history_order_fragment.DailyOrdersFragment
import com.codesroots.satavendor.presentation.history_order_fragment.HistoryOrderFragment
import com.codesroots.satavendor.presentation.new_order_bottomfragment.NewOrderFragment
import com.codesroots.satavendor.presentation.profile_fragment.ProfileFragment
import com.codesroots.satavendor.presentation.profile_fragment.ProfileFragment.Companion.TAG
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.satafood.core.entities.token.Token
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.map_activity.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.custom.onUiThread
import java.io.IOException
import java.net.URL
import javax.inject.Inject


class MapActivity : AppCompatActivity(), HasAndroidInjector, OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var Pref: PreferenceHelper
    internal var mFusedLocationClient: FusedLocationProviderClient? = null

    var map: GoogleMap? = null

    var latitude: Double? = null //-33.867
    var longitude: Double? = null // 151.206

    val overlaySize = 100f
    var address = ""

    var mDrawerLayout: DrawerLayout? = null

    var homeLatLng = LatLng(0.0, 0.0)


    var intent1: Intent? = null

    var mSocket: Socket? = null
    var data: ArrayList<OrdersItem>? = null

    var userLocationMarker: Marker? = null
    var isConnected = false;

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var distance: Double? = null

    var locationRequest: LocationRequest? = null

    lateinit var pDialog: SweetAlertDialog

    val viewModel by viewModels<CurrentOrderViewModel> { viewModelFactory }
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

    public override fun onCreate(icicle: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(icicle)
        PreferenceHelper(this)

        val binding: MapActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.map_activity)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true;

        mDrawerLayout = binding.drawerLayout

        statusCheck()

        var headerBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))


        ////////////// Socket ///////////////////////
        val app: BaseApplication = application as BaseApplication
        mSocket = app.getMSocket()
        //connecting socket
        mSocket?.connect()
        val options = IO.Options()
        options.reconnection = true //reconnection
        options.forceNew = true


        socket.on(Socket.EVENT_CONNECT) {
            try {
                restaurantStatus(" ????????",true,R.drawable.online_ic)
                Log.d("TAG", "socket// ${"connect"}")

            } catch (e: Exception) {
                Log.d(TAG, e.message!!)
            }
        }.on(Socket.EVENT_CONNECT_ERROR) {
            val e = it[0]
            Log.e(TAG, "error $e")
            runOnUiThread {
            WARN_MotionToast("error $e", this)
            }
        }.on(Socket.EVENT_DISCONNECT) {
            val e = it[0]
            Log.e(TAG, "Transport error $e")
                runOnUiThread {
                restaurantStatus("?????? ????????",false,R.drawable.offline_ic)
                    WARN_MotionToast("?????? ????????", this)

                Log.d("TAG", "socket// ${mSocket?.connected()}")
            }
        }

        mSocket?.emit("CreateDeliveryRoom", Pref.room_id!!)
        connectToSocket()
        mSocket?.on("makeNewOrderToBranch") {
            var mp = MediaPlayer.create(this, R.raw.alarm);
            mp.start();
            runOnUiThread {
                val gson = Gson()
                val json = it.first().toString()
                val type = object : TypeToken<OrdersItem?>() {}.type
                val newitem = gson.fromJson<OrdersItem>(json, type)
                data?.add(0, newitem)
                ClickHandler().openDialogFragment(this, NewOrderFragment(newitem!!, viewModel), "")
                Log.d("socket", json)

            }

        }

        ////// Delivery Status online/offline ///////////
        viewModel.getBranchData(Pref.VendorId!!)
        try {
            viewModel.deliveryItemLD!!.observe(this) {
                if (!it.isNullOrEmpty()) {
                    headerBinding.data = it[0]
                    nav_view.getHeaderView(0).userName.text = it[0].name?.replace("\"", "")
                } else
                    WARN_MotionToast("?????? ????????", this)
            }
        } catch (e: java.lang.Exception) {

        }
        getClientAddress()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.getHeaderView(0).switch1
            ?.setOnClickListener {
                if (nav_view.getHeaderView(0).switch1.isChecked) {
                    // The switch enabled
                    mSocket?.connect()
                    restaurantStatus(" ????????",true,R.drawable.online_ic)
                    Log.d("TAG", "socket// ${mSocket?.connected()}")

                } else {
                    // The switch disabled
                    mSocket?.disconnect()
                    runOnUiThread {
                        restaurantStatus("?????? ????????",false,R.drawable.offline_ic)
                        WARN_MotionToast("?????? ????????", this)
                        Log.d("TAG", "socket// ${mSocket?.connected()}")

                    }
                }
            }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mDrawerLayout!!.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerStateChanged(newState: Int) {
                if (newState == DrawerLayout.STATE_SETTLING && !mDrawerLayout!!.isDrawerOpen(
                        GravityCompat.START
                    )
                ) {
                    viewModel.getBranchData(Pref.VendorId!!)

                }
            }
        })

        siteDrawerMenuButton.setOnClickListener { view ->
            this.openCloseNavigationDrawer(view)
            note.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.note));
            if (MapHelper().CheckPermission(this)) {
                if (MapHelper().isLocationEnabled(this)) {
                    enableMyLocation(this)
                } else {

                    Toast.makeText(
                        this,
                        "Please Turn on Your device Location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.packageName, null),
                        ),
                    )
                }
            }
        }

        note.setOnClickListener {
            note.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.note_active));
            ClickHandler().openDialogFragment(this, CurrentOrderFragment(viewModel), "")

        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG", "token:///:false ${task.exception?.message}")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
           viewModel.updateUserToken(Pref.userId!!,Token(token))
            Log.d("TAG", "token:///:"  + Pref.VendorId +"///"+ token)
            if (!Pref.UserToken.isNullOrEmpty()) {
                lifecycleScope.launch {

                }
            }
            // Log and toast
        })
    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                        // selected Don't ask again
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }



    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_LOCATION -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(
//                            this,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        mFusedLocationClient?.requestLocationUpdates(
//                            locationRequest,
//                            locationCallback,
//                            Looper.getMainLooper()
//                        )
//
//                        // Now check background location
//                        checkBackgroundLocation()
//                    }
//
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
//
//                    // Check if we are in a state where the user has denied the permission and
//                    // selected Don't ask again
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                            this,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        )
//                    ) {
//                        startActivity(
//                            Intent(
//                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                Uri.fromParts("package", this.packageName, null),
//                            ),
//                        )
//                    }
//                }
//                return
//            }
//
//        }
//    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.clear();
        MapHelper().setPoiClick(map!!)
        //MapHelper().setMapStyle(map, this)
        //  statusCheck()

        //  getLocationPermission()
        // Add a marker in Sydney and mDrawermove the camera

        //  map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0), 16.0f))
        MapHelper().RequestPermission(this)



        if (MapHelper().CheckPermission(this)) {
            if (MapHelper().isLocationEnabled(this)) {
                enableMyLocation(this)
            } else {

                Toast.makeText(
                    this,
                    "Please Turn on Your device Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            MapHelper().RequestPermission(this)

        }
    }


    @SuppressLint("MissingPermission")
    fun enableMyLocation(context: Context) {
        map?.isMyLocationEnabled = true
        map?.isTrafficEnabled = true
        mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location == null) {
                MapHelper().NewLocationData(context)
            }

            latitude = location!!.latitude
            longitude = location.longitude

            map!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        latitude!!,
                        longitude!!
                    ), 16.0f
                )
            )
            homeLatLng = LatLng(latitude!!, longitude!!)

        }

    }

    fun startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mFusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()

        )
    }

    fun stopLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
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

        mFusedLocationClient?.removeLocationUpdates(locationCallback)

    }

    fun setUserLocationMarker(location: Location) {
        homeLatLng = LatLng(location.latitude, location.longitude)
        if (userLocationMarker == null) {
            userLocationMarker = map!!.addMarker(
                MarkerOptions()
                    .position(homeLatLng)
                    .icon(
                        BitmapDescriptorFactory
                            .fromResource(R.drawable.restaurant_ic)
                    )
//                .rotation(location.bearing)
//                .anchor(0.5f, 0.5f)
            )

        } else {
            userLocationMarker!!.position = homeLatLng
            //        userLocationMarker!!.rotation = location.bearing


        }
    }

    fun updateLocation() {
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
                    Log.d(TAG, "onLocationResult: " + locationResult.lastLocation)

                    if (map != null)
                        setUserLocationMarker(locationResult.lastLocation)

                    //Showing the latitude, longitude and accuracy on the home screen.
                    //      for (location in locationResult.locations) {
                    // map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng( locationResult.lastLocation.latitude, locationResult.lastLocation.longitude), 16.0f))
//                    for (location in locationResult.locations) {
//                        latitude = locationResult.lastLocation.latitude
//                        longitude = locationResult.lastLocation.longitude
//                        homeLatLng = LatLng(latitude!!, longitude!!)
//
//           //             viewModel.intents.trySend(MainIntent.getLatLong(viewModel.state.value!!.copy(cliendLatitude = latitude,cliendLongitude = longitude,progress = true)))
//
//
//
//                    }

                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            try {


            } catch (e: Exception) {

            }

            return
        }

//        if (map != null && map!!.cameraPosition.zoom == 16.0f && map!!.cameraPosition.zoom > 3.0f) {
//            Toast.makeText(this, "a", Toast.LENGTH_SHORT).show()
//            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
//
//        } else {
        mFusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        //    }

    }

    @SuppressLint("MissingPermission")
    private fun getLocationPermission() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener(this,
            OnSuccessListener<Location?> { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    goToAddress(latitude!!, longitude!!)
                    homeLatLng = LatLng(latitude!!, longitude!!)

                }
            })
    }

    private fun goToAddress(mlatitude: Double, mLogitude: Double) {
        try {
            val homeLatLng = LatLng(mlatitude, mLogitude)
            map?.clear()
            map!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(mlatitude, mLogitude),
                    16.0f
                )
            )

            map!!.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
                latitude = map!!.cameraPosition.target.latitude
                longitude = map!!.cameraPosition.target.longitude
            })
        } catch (e: Exception) {
            checkLocationPermission()
        }

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
                                if (userLocationMarker == null) {
                                    userLocationMarker = map!!.addMarker(
                                        MarkerOptions()
                                            .position(homeLatLng)
                                            .icon(
                                                BitmapDescriptorFactory
                                                    .fromResource(R.drawable.motor_ic)
                                            )
//                                        .rotation(location.bearing)
//                                        .anchor(0.5f, 0.5f)
                                    )

                                } else {
                                    userLocationMarker!!.position = homeLatLng
//                                    userLocationMarker!!.rotation = location.bearing

                                }
//                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
//                                    end_latitude,
//                                    end_longitude), 16.0f))

                                map!!.addMarker(MarkerOptions().position(clientLatLng))

                                val options = PolylineOptions()
                                options.color(this@MapActivity.getColor(R.color.black))
                                options.width(10f)
                                val url = getURL(homeLatLng, clientLatLng)
                                async {
                                    // Connect to URL, download content and convert into string asynchronously
                                    val result = URL(url).readText()
                                    val LatLongB = LatLngBounds.Builder()

                                    onUiThread {
                                        // When API call is done, create parser and convert into JsonObjec
                                        val parser = Parser()
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
                                                    decodePoly(
                                                        it.obj("polyline")
                                                            ?.string("points")!!
                                                    )
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
                                            map!!.addPolyline(options)
                                            // show map with route centered
                                            map!!.moveCamera(
                                                CameraUpdateFactory.newLatLngBounds(
                                                    bounds,
                                                    60
                                                )
                                            )
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

        return "https://maps.googleapis.com/maps/api/directions/json?$params&key=AIzaSyBxgJFTad1Ir0m_pn7dfzm3qVGnK7IyoFQ"

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

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.current_orders -> {
                // Handle the camera action
                ClickHandler().openDialogCurrentOrderFragment(
                    this,
                    CurrentOrderFragment(viewModel),
                    CurrentOrderFragment.TAG, viewModel
                )
            }
            R.id.dailyOrder -> {
                ClickHandler().openDialogFragment(
                    this,
                    DailyOrdersFragment(),
                    DailyOrdersFragment.TAG
                )
            }
            R.id.archiveOrders -> {
                ClickHandler().openDialogFragment(
                    this,
                    HistoryOrderFragment(),
                    HistoryOrderFragment.TAG
                )
            }
//            R.id.nav_tools -> {
//
//            }
            R.id.logout -> {
                Pref.UserToken = ""
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()

            }
            R.id.profile -> {
                ClickHandler().switchBetweenFragments(this, ProfileFragment())

            }
        }


        mDrawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    fun openCloseNavigationDrawer(view: View) {

        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)

        } else {
            mDrawerLayout!!.openDrawer(GravityCompat.START)
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
        updateLocation()
        connectToSocket()



    }


    fun restaurantStatus(resStatus:String,isChecked:Boolean,icon:Int){
        nav_view.getHeaderView(0).switch1.isChecked = isChecked
        status.text = resStatus
        statusIcon.setImageResource(icon)
    }
    private fun connectToSocket() {
        mSocket?.connect()
        mSocket?.emit("create_user", Pref.VendorId)

    }

}