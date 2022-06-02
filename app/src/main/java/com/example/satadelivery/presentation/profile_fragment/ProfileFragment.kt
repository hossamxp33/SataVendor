package com.example.satadelivery.presentation.profile_fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.satadelivery.R
import com.example.satadelivery.databinding.CurrentItemBinding
import com.example.satadelivery.databinding.ProfileFragmentBinding
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.helper.PreferenceHelper
import com.example.satadelivery.models.auth.Driver
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.example.satadelivery.presentation.details_order_fragment.DetailsOrderFragment
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.example.satadelivery.presentation.profile_fragment.viewmodel.ProfileViewmodel
import kotlinx.android.synthetic.main.delivery_login_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.*

import javax.inject.Inject

class ProfileFragment @Inject constructor() : Fragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<ProfileViewmodel> { viewModelFactory }


    @Inject
    lateinit var pref: PreferenceHelper

    lateinit var view: ProfileFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        view = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_fragment, container, false
        )


        view.listener = ClickHandler()
        view.context = context as MapActivity
        view.pref = (context as MapActivity).Pref



        return view.root
    }

    fun editRequest() {
        val deliveryInfo = Driver(
          photo = "",  name = username.text.toString(), mobile = mobileNumber.text.toString())
        viewModel.editDeliveryData(1,deliveryInfo)
    }


}