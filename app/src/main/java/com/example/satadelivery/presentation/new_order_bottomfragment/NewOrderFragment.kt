package com.example.satadelivery.presentation.new_order_bottomfragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.satadelivery.MainActivity
import com.example.satadelivery.R
import com.example.satadelivery.databinding.NeworderFragmentBinding
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class NewOrderFragment @Inject constructor(var item: OrdersItem) : DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }


    lateinit var view: NeworderFragmentBinding
    var SelectedSortOption = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = DataBindingUtil.inflate(inflater,
            R.layout.neworder_fragment, container, false)
        //   view.listener = ClickHandler()

        view.context = context as MapActivity
        view.data = item

        return view.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = 1800
        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (savedInstanceState == null) {
            dialog.window?.setWindowAnimations(
                R.style.Animation_Design_BottomSheetDialog
            )
        } else {
            dialog.window?.setWindowAnimations(
                R.style.Animation_Design_BottomSheetDialog
            )
        }


        return dialog
    }
}