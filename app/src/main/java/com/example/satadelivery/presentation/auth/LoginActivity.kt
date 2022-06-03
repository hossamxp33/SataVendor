package com.example.satadelivery.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.satadelivery.R
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.PreferenceHelper
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.delivery_login_fragment.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    public var mSocket: Socket? = null

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var Pref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        PreferenceHelper(this)

        setContentView(R.layout.delivery_login_fragment)

        val app: BaseApplication = application as BaseApplication
        mSocket = app.getMSocket()
        mSocket?.connect()
        login.setOnClickListener {
            loginRequest()
        }

//         if (Pref.VendorId != 0)
//             supportFragmentManager.beginTransaction()
//             .replace(R.id.login_frame, BranchOrdersFragment()).addToBackStack(null).commit()

        viewModel.authLD?.observe(this, Observer {

            if (it.token == null) {
                Toast.makeText(this, "خطأ في كلمة المرور او كلمة السر", Toast.LENGTH_SHORT).show()

            } else {

                Pref.UserToken = it.token
                Pref.deliveryId = it.user.driver!!.id!!
                Pref.restaurantName = it.user.name
                Pref.userName = it.user.driver!!.name
                Pref.userPhone = it.user.driver!!.mobile
                Pref.photo = it.user.driver!!.photo
                Pref.room_id = it.user.room_id

                val mainIntent = Intent(this, MapActivity::class.java)
                startActivity(mainIntent)
                finish()

            }

        })


        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, "خطأ في كلمة المرور او كلمة السر", Toast.LENGTH_SHORT).show()
        })


    }


    fun loginRequest() {
        val loginInfo = User(
            username = username.text.toString(), password = password.text.toString())
        viewModel.login(loginInfo)
    }


}