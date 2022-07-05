package com.codesroots.satavendor.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class PreferenceHelper(private val context: Context) {

    private var app_prefs: SharedPreferences? = null
    private val app_ref = "userdetails"
    private val IsActive = "IsActive"
    var latitude: String?
        get() = app_prefs!!.getString("latitude", "")
        set(latitude) {
            val edit = app_prefs!!.edit()

            edit.putString("latitude", latitude)
            edit.apply()
        }
    var longitude: String?
        get() = app_prefs!!.getString("longitude", "")
        set(longitude) {
            val edit = app_prefs!!.edit()

            edit.putString("longitude", longitude)
            edit.apply()
        }
    var cartResuturantId: Int
        get() = app_prefs!!.getInt("cartResuturantId", 0)
        set(cartResuturantId) {
            var edit = app_prefs!!.edit()
            edit.putInt("cartResuturantId", cartResuturantId)
            edit.apply()
        }
    var deliveryId: Int
        get() = app_prefs!!.getInt("deliveryId", 0)
        set(deliveryId) {
            var edit = app_prefs!!.edit()
            edit.putInt("deliveryId", deliveryId)
            edit.apply()
        }

    var delivery_status: Int
        get() = app_prefs!!.getInt("delivery_status", 0)
        set(delivery_status) {
            var edit = app_prefs!!.edit()
            edit.putInt("delivery_status", delivery_status)
            edit.apply()
        }

    var restaurantId: Int
        get() = app_prefs!!.getInt("restaurantId", 0)
        set(restaurantId) {
            var edit = app_prefs!!.edit()
            edit.putInt("restaurantId", restaurantId)
            edit.apply()
        }
    var BranchId: Int
        get() = app_prefs!!.getInt("BranchId", 0)
        set(BranchId) {
            var edit = app_prefs!!.edit()
            edit.putInt("BranchId", BranchId)
            edit.apply()
        }
    var CityId: Int
        get() = app_prefs!!.getInt("CityId", 0)
        set(CityId) {
            var edit = app_prefs!!.edit()
            edit.putInt("CityId", CityId)
            edit.apply()
        }
    var userName: String?
        get() = app_prefs!!.getString("userName", "Alaa")
        set(userName) {
            val edit = app_prefs!!.edit()
            edit.putString("userName", userName)
            edit.apply()
        }
    var photo: String?
        get() = app_prefs!!.getString("photo", "")
        set(photo) {
            val edit = app_prefs!!.edit()
            edit.putString("photo", photo)
            edit.apply()
        }
    var userAddress: String?
        get() = app_prefs!!.getString("userAddress", "Address")
        set(userAddress) {
            val edit = app_prefs!!.edit()
            edit.putString("userAddress", userAddress)
            edit.apply()
        }
    var UserToken: String?
        get() = app_prefs!!.getString("UserToken", "")
        set(UserToken) {
            val edit = app_prefs!!.edit()
            edit.putString("UserToken", UserToken)
            edit.apply()
        }
    var room_id: String?
        get() = app_prefs!!.getString("room_id", "")
        set(room_id) {
            val edit = app_prefs!!.edit()
            edit.putString("room_id", room_id)
            edit.apply()
        }

    var restaurantLat: String?
        get() = app_prefs!!.getString("restaurantLat", "")
        set(restaurantLat) {
            val edit = app_prefs!!.edit()
            edit.putString("restaurantLat", restaurantLat)
            edit.apply()
        }

    var restaurantLong: String?
        get() = app_prefs!!.getString("restaurantLong", "")
        set(restaurantLong) {
            val edit = app_prefs!!.edit()
            edit.putString("restaurantLong", restaurantLong)
            edit.apply()
        }

    var userLocation: String?
        get() = app_prefs!!.getString("userLocation", "0")
        set(userLocation) {
            val edit = app_prefs!!.edit()
            edit.putString("userLocation", userLocation)
            edit.apply()
        }
    var googleID: String?
        get() = app_prefs!!.getString("googleID", "ChIJAWPGlF0-WBQRbCBC3gDx3d0")
        set(googleID) {
            val edit = app_prefs!!.edit()
            edit.putString("googleID", googleID)
            edit.apply()
        }
    var userPhone: String?
        get() = app_prefs!!.getString("userPhone", "0")
        set(userPhone) {
            val edit = app_prefs!!.edit()
            edit.putString("userPhone", userPhone)
            edit.apply()
        }


    var UserId: Int
        get() = app_prefs!!.getInt("UserId", 0)
        set(UserId) {
            var edit = app_prefs!!.edit()
            edit.putInt("UserId", UserId)
            edit.apply()
        }

    var restaurantName: String?
        get() = app_prefs!!.getString("restaurantName", "")
        set(restaurantName) {
            val edit = app_prefs!!.edit()
            edit.putString("restaurantName", restaurantName)
            edit.apply()
        }
    var lang: String?
        get() = app_prefs!!.getString("lang", "ar")
        set(lang) {
            val edit = app_prefs!!.edit()
            edit.putString("lang", lang)
            edit.apply()
        }

    var VendorId: Int?
        get() = app_prefs!!.getInt("VendorId", 0)
        set(VendorId) {
            val edit = app_prefs!!.edit()
            edit.putInt("VendorId", VendorId!!)
            edit.apply()
        }
    private val arrPackage = listOf<String>()
    private val CART_ARRAY = "CART_ARRAY"
    private val Options_ARRAY = "Options_ARRAY"

    fun addOptionstoCart(Options: ArrayList<Int>) {
        val gson = Gson()
        val json = app_prefs!!.getString(Options_ARRAY, listOf<Int>().toString())
        val type = object : TypeToken<List<String?>?>() {}.type
        var arrayList = gson.fromJson<ArrayList<Int>>(json, type)
        val editor = app_prefs!!.edit()
        arrayList.addAll(Options)
        val newdata = gson.toJson(arrayList, type)
        editor.putString(Options_ARRAY, newdata)
        editor.apply()
        editor.commit()
    }


    init {
        try {
            app_prefs = context.getSharedPreferences(
                app_ref,
                Context.MODE_PRIVATE
            )
        } catch (e: NullPointerException) {
        }
    }
}