package com.example.satadelivery.presentation.history_order_fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.satadelivery.MainActivity
import com.example.satadelivery.R
import com.example.satadelivery.databinding.HistoryOrdersFragmentBinding
import com.example.satadelivery.databinding.NeworderFragmentBinding
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class HistoryOrderFragment @Inject constructor() : DialogFragment() {

    companion object { const val TAG = "TownBottomSheetDialogFragment" }


    lateinit var view: HistoryOrdersFragmentBinding
var SelectedSortOption = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = DataBindingUtil.inflate(inflater,
            R.layout.history_orders_fragment, container, false)
     //   view.listener = ClickHandler()
        view.context = context as MapActivity




        return view.root
    }
    override fun onStart() {
        super.onStart()
        setWindowParams()
    }
    private fun setWindowParams(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }




}