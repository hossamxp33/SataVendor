package com.sata.satadelivery.helper

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.sata.satadelivery.R
import com.google.android.material.imageview.ShapeableImageView
import www.sanju.motiontoast.MotionToast
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*


///TOAST_SUCCESS_MotionToast
fun SUCCESS_MotionToast(massage: String, context: Activity) {
    MotionToast.createColorToast(
        context,
        "تم بنجاح",
        massage,
        MotionToast.TOAST_SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, R.font.helvetica_regular)
    )
}
fun WARN_MotionToast(massage: String, context: Activity) {
    MotionToast.createColorToast(
        context,
        context.getString(R.string.text_error),
        massage,
        MotionToast.TOAST_WARNING,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, R.font.helvetica_regular)
    )
}
fun Error_MotionToast(massage: String, context: Activity) {
    MotionToast.createColorToast(
        context,
        "يوجد خطأ",
        massage,
        MotionToast.TOAST_NO_INTERNET,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, R.font.helvetica_regular)
    )
}


@BindingAdapter("app:imageResource")
fun setImageResource(imageView: AppCompatImageView, resource: String?) {
    val requestOptions = RequestOptions()
    requestOptions.error(R.drawable.note)
    Glide.with(imageView.context)
        .load((Constants.IMAGE_URL + resource))
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(requestOptions)
        .into(imageView)
}

fun SetImageResource(imageView: AppCompatImageView, resource: String?) {
    val requestOptions = RequestOptions()
    requestOptions.error(R.drawable.note)
    Glide.with(imageView.context)
        .load((Constants.IMAGE_URL + resource))
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(requestOptions)
        .into(imageView)
}

@BindingAdapter("text_color")
/////// set Stock image statue /////
fun setTextStock(text: TextView, color: String?) {
    when (color){
        "(مفتوح)"  ->
            text.setTextColor(Color.parseColor("#4CAF50"))


        "(مغلق)" ->  text.setTextColor(Color.parseColor("#FF0000"))

        else -> {
            text.setTextColor(Color.parseColor("#9C9898"))
        }
    }

}

@BindingAdapter("app:datetext")
fun setDatetext(text: TextView, resource: String?) {

    val myFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")

    val dateObj: Date
try {

    dateObj = myFormat.parse(resource)
    val timestamp = dateObj.time.toString()//  //Example -> in ms
    val fromServer = SimpleDateFormat(" HH:mm", Locale("en"))
    val dateString = fromServer.format(Date(Long.parseLong(timestamp)))

    text.text = dateString
}catch (e:Exception){}
}

fun ShapeAppearanceRounded(context: Context, view: ShapeableImageView) {
    val radius = context.resources.getDimension(R.dimen.dimen_48)
    val shapeAppearanceModel = view.shapeAppearanceModel.toBuilder()
        .setAllCornerSizes(radius)
        .build()
    view.shapeAppearanceModel = shapeAppearanceModel

}

fun <T> androidLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)
inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
    ViewModelProviders.of(this, viewModelFactory)[T::class.java]

