package com.codesroots.satavendor.helper

import android.content.Context
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.DeliveryLoginFragmentBinding
import com.codesroots.satavendor.presentation.auth.LoginActivity
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