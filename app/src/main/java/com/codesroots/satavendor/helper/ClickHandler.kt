package com.codesroots.satavendor.helper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.codesroots.satavendor.R
import com.codesroots.satavendor.presentation.current_order_fragment.CurrentOrderFragment
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.codesroots.satavendor.presentation.map_activity.MapActivity


public class ClickHandler {

    var Pref: PreferenceHelper? = null
    var context: Context? = null

    fun callNumber(number: String, context: Context) {

        val intent = Intent(android.content.Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:" + number)

        if (Build.VERSION.SDK_INT > 23) {
            context.startActivity(intent)
        } else {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Permission Not Granted ", Toast.LENGTH_SHORT).show()
            } else {
                val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.CALL_PHONE)
                ActivityCompat.requestPermissions(context as MapActivity, PERMISSIONS_STORAGE, 9)
                context.startActivity(intent)
            }
        }
    }

    fun openDialogCurrentOrderFragment(context: Context, fragment: DialogFragment, tag:String,viewModel: CurrentOrderViewModel
    ) {
        fragment.apply {
            ( fragment as CurrentOrderFragment).viewModel = viewModel
            show((context as MapActivity).supportFragmentManager,tag)
        }
    }


    fun openDialogFragment(context: Context, fragment: DialogFragment, tag:String) {
        val fragmentTransaction = (context as MapActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.add(fragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }



    fun switchBetweenFragments(context: Context, fragment: Fragment) {
        (context as MapActivity)
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(0, 0, 0, 0)
            .replace(R.id.main_frame, fragment).addToBackStack(null).commit()
    }

}


