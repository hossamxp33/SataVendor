package com.codesroots.satavendor.presentation.splashScreen


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.codesroots.satavendor.R
import com.codesroots.satavendor.helper.PreferenceHelper
import com.codesroots.satavendor.presentation.auth.LoginActivity
import com.codesroots.satavendor.presentation.map_activity.MapActivity

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class SplashScreen : AppCompatActivity() , HasAndroidInjector {


    private val SPLASH_DISPLAY_LENGTH = 2000 //splash screen will be shown for 2 seconds

    @Inject
    lateinit var Pref: PreferenceHelper

    public override fun onCreate(icicle: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(icicle)

        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
             if (!Pref.UserToken.isNullOrEmpty())
             {
                 val mainIntent = Intent(this, MapActivity::class.java)
                 startActivity(mainIntent)
                 finish()
             }
             else{
                 val mainIntent = Intent(this, LoginActivity::class.java)
                 startActivity(mainIntent)
                 finish()
             }

        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

}