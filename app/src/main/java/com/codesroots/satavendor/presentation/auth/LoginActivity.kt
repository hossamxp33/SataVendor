package com.codesroots.satavendor.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.DeliveryLoginFragmentBinding
import com.codesroots.satavendor.helper.*
import com.codesroots.satavendor.models.auth.User
import com.codesroots.satavendor.presentation.map_activity.MapActivity
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
    lateinit var pref: PreferenceHelper
    var isAllFieldsChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        PreferenceHelper(this)

        setContentView(R.layout.delivery_login_fragment)
        val binding: DeliveryLoginFragmentBinding =
            DataBindingUtil.setContentView(this, R.layout.delivery_login_fragment)
        val app: BaseApplication = application as BaseApplication
        mSocket = app.getMSocket()
        mSocket?.connect()

        binding.btnLogin.setOnClickListener {
            isAllFieldsChecked = Validation().checkAllFields(binding,this);
            if (isAllFieldsChecked) {
                loginRequest()
                progress.isVisible = true
            }else
                Error_MotionToast("أكمل البيانات", this)
        }

//         if (pref.VendorId != 0)
//             supportFragmentManager.beginTransaction()
//             .replace(R.id.login_frame, BranchOrdersFragment()).addToBackStack(null).commit()

        viewModel.authLD?.observe(this, Observer {

            if (it.token == null) {
                Toast.makeText(this, "خطأ في كلمة المرور او كلمة السر", Toast.LENGTH_SHORT).show()
                progress.isVisible = false

            } else {

                pref.UserToken = it.token
                pref.VendorId = it.user!!.branches?.id
                pref.restaurantName = it.user!!.branches?.name
                pref.userPhone = it.user!!.branches?.phone
                pref.room_id = it.user!!.room_id
                pref.userId = it.user!!.id!!
                pref.restaurantLat = it.user!!.branches?.latitude.toString()
                pref.restaurantLong = it.user!!.branches?.longitude.toString()
                progress.isVisible = false
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