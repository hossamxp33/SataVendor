package com.sata.satadelivery.helper

import android.content.Context
import com.sata.satadelivery.R
import com.sata.satadelivery.databinding.DeliveryLoginFragmentBinding
import com.sata.satadelivery.presentation.auth.LoginActivity
import kotlinx.android.synthetic.main.delivery_login_fragment.*
import kotlinx.android.synthetic.main.nav_header_main.*


class Validation {
    fun checkAllFields(view:  DeliveryLoginFragmentBinding, context: Context): Boolean {
        context as LoginActivity
        if (view.username.length() == 0) {
            view.username.error = context.getString(R.string.required);
            return false;
        }

        if (view.password.length() == 0) {
            view.password.error = context.getString(R.string.required);
            return false;
        }


        return true;
    }
}