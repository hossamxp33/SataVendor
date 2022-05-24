package com.example.satadelivery.helper

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.satadelivery.R
import com.example.satadelivery.presentation.current_order_fragment.CurrentOrderFragment
import com.example.satadelivery.presentation.daily_order_fragment.DailyOrderFragment
import com.example.satadelivery.presentation.map_activity.MapActivity


class ClickHandler {

    var Pref: PreferenceHelper? = null
    var context: Context? = null


    fun OpenMyFragment(context: Context,fragment: DialogFragment,tag:String) {
        fragment.apply {
            show((context as MapActivity).supportFragmentManager,tag)
        }
    }
    fun SwitchBetweenFragments(context: Context, fragment: Fragment) {
        (context as MapActivity)
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(0, 0, 0, 0)
            .replace(R.id.main_frame, fragment).addToBackStack(null).commit()
    }

}


