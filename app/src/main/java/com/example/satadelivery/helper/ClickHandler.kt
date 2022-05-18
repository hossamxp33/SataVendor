package com.example.satadelivery.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.satadelivery.R
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.example.satadelivery.presentation.new_order_bottomfragment.NewOrderFragment


class ClickHandler {


    var Pref: PreferenceHelper? = null
    var context: Context? = null





    fun OpenMyFragment(context: Context) {
        val frag = NewOrderFragment()

        frag.apply {
            show((context as MapActivity).supportFragmentManager, NewOrderFragment.TAG)
        }
    }

}


