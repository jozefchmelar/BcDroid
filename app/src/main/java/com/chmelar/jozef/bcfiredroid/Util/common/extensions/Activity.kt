package extensions.android

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

val Context.inputMethodManager get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Activity.screenWidthPixels get() = DisplayMetrics().apply { windowManager.defaultDisplay.getMetrics(this) }.widthPixels
fun Activity.hideKeyboard(): Unit { inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0) }

fun Activity.contentView(@LayoutRes layout: Int, viewLogic: View.() -> Unit) =
    layoutInflater.inflate(layout, null).apply(viewLogic).let(this::setContentView)

fun Activity.transparentStatusBar() {
    if (Build.VERSION.SDK_INT >= 21) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.statusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(applicationContext, color)
    }
}

fun Activity.transparentStatusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = ContextCompat.getColor(applicationContext, color)
    }
}