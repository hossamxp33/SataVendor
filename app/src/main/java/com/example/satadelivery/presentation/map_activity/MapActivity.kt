package com.example.satadelivery.presentation.map_activity


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.setupWithNavController
import com.example.satadelivery.R
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.helper.MapHelper
import com.example.satadelivery.helper.PreferenceHelper
import com.example.satadelivery.presentation.new_order_bottomfragment.NewOrderFragment

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.map_activity.*
import org.jetbrains.anko.custom.async
import java.net.URL
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException
import java.util.*
import javax.inject.Inject
import com.beust.klaxon.*
import com.example.satadelivery.presentation.current_order_fragment.CurrentOrderFragment
import org.jetbrains.anko.custom.onUiThread

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
    var streetName: String? = null
    var place_id = ""
    var mDrawerLayout: DrawerLayout? = null

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
        mDrawerLayout = findViewById(R.id.drawerLayout)

//        val toggle = ActionBarDrawerToggle(
//            this,
//            mDrawerLayout,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        mDrawerLayout?.addDrawerListener(toggle)
//
//        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)

//        menu.setOnClickListener {
//           openCloseNavigationDrawer()
//        }
        siteDrawerMenuButton.setOnClickListener { view ->
            this.openCloseNavigationDrawer(view)

        }

        }













    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }


    private fun getURL(from : LatLng, to : LatLng) : String {
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
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.clear();
        MapHelper().setPoiClick(map)
//        MapHelper().setMapStyle(map, this)
        getLocationPermission()
        val LatLongB = LatLngBounds.Builder()

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val opera = LatLng(-33.9320447,151.1597271)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.addMarker(MarkerOptions().position(opera).title("Opera House"))
    //    map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0), 16.0f))


        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)
        val url = getURL(sydney, opera)
        try {
//        async {
//            // Connect to URL, download content and convert into string asynchronously
//            val result = URL(url).readText()
//            onUiThread {
//                // When API call is done, create parser and convert into JsonObjec
//                val parser: Parser = Parser()
//                val stringBuilder: StringBuilder = StringBuilder(result)
//                val json: com.beust.klaxon.JsonObject = parser.parse(stringBuilder) as com.beust.klaxon.JsonObject
//                // get to the correct element in JsonObject
//                try {
//
//                val routes = json.array<com.beust.klaxon.JsonObject>("routes")
//
//                val points = routes!!["legs"]["steps"][0] as com.beust.klaxon.JsonArray<com.beust.klaxon.JsonObject>
//
//                // For every element in the JsonArray, decode the polyline string and pass all points to a List
//
//                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)  }
//                // Add  points to polyline and bounds
//
//                options.add(sydney)
//                LatLongB.include(sydney)
//                for (point in polypts)  {
//                    options.add(point)
//                    LatLongB.include(point)
//                }
//                options.add(opera)
//                LatLongB.include(opera)
//                // build bounds
//                val bounds = LatLongB.build()
//                // add polyline to the map
//                map.addPolyline(options)
//                // show map with route centered
//                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))  }catch (e:Exception){}
//            }
//        }

        }catch (e:Exception){}
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

            if (addresses != null && addresses.isNotEmpty()) {
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

        return null
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.current_orders -> {
                // Handle the camera action
                ClickHandler().OpenMyFragment(this,CurrentOrderFragment(),CurrentOrderFragment.TAG)
            }
            R.id.dailyOrder -> {
                Toast.makeText(this, "nav_gallery", Toast.LENGTH_SHORT).show()
            }
            R.id.archiveOrders -> {

            }
            R.id.nav_tools -> {

            }
            R.id.logout -> {
                ClickHandler().OpenMyFragment(this, NewOrderFragment(),NewOrderFragment.TAG)

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

}