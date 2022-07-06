package com.codesroots.satavendor.presentation.order_times_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.LayoutDialogOrderTimeBinding
import com.codesroots.satavendor.helper.HorizontalMarginItemDecoration
import com.codesroots.satavendor.helper.dpToPx

class OrderTimesDialog private constructor(context: Context, themeResId: Int, private val onItemClick: (Int?) -> Unit) :
    Dialog(context, themeResId) {

    private var _binding: LayoutDialogOrderTimeBinding? = null
    private val binding get() = _binding!!

    lateinit var orderTimesAdapter: OrderTimesAdapter

    companion object {
        private var loadingProgressBarDialog: OrderTimesDialog? = null

        @SuppressLint("ResourceAsColor")
        public fun getInstance(context: Context, onItemClick: (Int?) -> Unit): OrderTimesDialog {
//            if(loadingProgressBarDialog == null) {
                loadingProgressBarDialog =
                    OrderTimesDialog(context, R.style.MaterialDialog, onItemClick)
                loadingProgressBarDialog!!.setCancelable(true)
                loadingProgressBarDialog!!.window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                loadingProgressBarDialog!!.window?.setGravity(Gravity.CENTER)
                loadingProgressBarDialog!!.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                loadingProgressBarDialog!!.setCanceledOnTouchOutside(true)
//                loadingProgressBarDialog!!.getWindow()
//                    ?.setBackgroundDrawable(ColorDrawable(Color.rgb(0, 45, 42)))
                loadingProgressBarDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            }
            return loadingProgressBarDialog!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.layout_dialog_order_time, null)
        _binding = LayoutDialogOrderTimeBinding.bind(view)
        setContentView(view)

        orderTimesAdapter = OrderTimesAdapter {
            Log.d("TAG", "orderTimesDialog: $it")
            onItemClick(it)
        }

        binding.rvTimes.apply {
            adapter = orderTimesAdapter
            addItemDecoration(
                HorizontalMarginItemDecoration(
                    dpToPx(10f, context)
                )
            )
        }

        orderTimesAdapter.submitList(getOrderTimes())
    }

    fun getOrderTimes(): MutableList<Int> {
        val times = mutableListOf<Int>()
        for (time in 5..60 step 5)
            times.add(time)

        return times
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}