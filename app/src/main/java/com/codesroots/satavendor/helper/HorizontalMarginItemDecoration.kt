package com.codesroots.satavendor.helper

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlin.math.roundToInt

class HorizontalMarginItemDecoration(
    private val spaceHeight: Int,
    private var arabicLanguage: Boolean = false
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) {
            if (arabicLanguage) {
                outRect.right = spaceHeight
                outRect.left = spaceHeight / 2
            } else {
                outRect.right = spaceHeight / 2
                outRect.left = spaceHeight
            }
        } else {
            outRect.left = spaceHeight / 2
            outRect.right = spaceHeight / 2
        }
        outRect.bottom = spaceHeight
    }
}
fun dpToPx(dp: Float, context: Context): Int {
    return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}