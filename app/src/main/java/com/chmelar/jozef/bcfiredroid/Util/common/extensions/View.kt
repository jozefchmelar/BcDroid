package extensions.android

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.annotation.MenuRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast

fun View.color(id: Int) = ContextCompat.getColor(context, id)
fun View.drawable(id: Int): Drawable = ContextCompat.getDrawable(context, id)
fun View.dimen(id: Int) = resources.getDimension(id)
fun View.string(id: Int) = resources.getString(id)
fun View.string(id: Int, vararg args: Any) = resources.getString(id, args)

fun View.beVisible()   { visibility = View.VISIBLE }
fun View.beInvisible() { visibility = View.INVISIBLE }
fun View.beGone()      { visibility = View.GONE }
fun View.beVisibleIf(shouldBeVisible: Boolean) { visibility = if(shouldBeVisible) View.VISIBLE else View.GONE }
fun View.beInvisibleIf(shouldBeInvisible: Boolean) { visibility = if(shouldBeInvisible) View.INVISIBLE else View.VISIBLE }
fun View.beGoneIf(shouldBeGone: Boolean) = beVisibleIf(!shouldBeGone)

fun View.scale(v: Float) = apply { scaleX = v; scaleY = v }
fun ViewPropertyAnimator.scale(v: Float) = scaleX(v).scaleY(v)

inline infix fun View.onClick(crossinline action: () -> Unit) = setOnClickListener { action() }
inline infix fun View.onTouchUp(crossinline action: (x: Int, y: Int) -> Unit) = setOnTouchListener { _, e -> if (e.action == MotionEvent.ACTION_UP) action(e.x.toInt(), e.y.toInt()); true }

fun View.px(dp: Int) = (dp * (resources.displayMetrics.densityDpi / 160f)).toInt()
fun View.dp(px: Int) = (px / (resources.displayMetrics.densityDpi / 160f)).toInt()

fun View.toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
fun View.toast(stringId: Int) = Toast.makeText(context, context.getString(stringId), Toast.LENGTH_SHORT).show()

val View.activity: AppCompatActivity get() =
if(context is AppCompatActivity) context as AppCompatActivity
else (context as ContextWrapper).baseContext as AppCompatActivity

inline fun <reified A: Activity> View.start() = context.startActivity(Intent(context, A::class.java))
inline fun <reified A: Activity> View.start(config: Intent.() -> Unit) = context.startActivity(Intent(context, A::class.java).apply(config))

inline fun View.onAttachState(crossinline onAttach: () -> Unit, crossinline onDetach: () -> Unit) = addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
    override fun onViewDetachedFromWindow(view: View) = onDetach()
    override fun onViewAttachedToWindow(view: View) = onAttach()
})

inline fun View.onAttach(crossinline action: () -> Unit) = onAttachState(action, {})
inline fun View.onDetach(crossinline action: () -> Unit) = onAttachState({}, action)

inline fun View.onMeasure(crossinline callback: (w: Int, h: Int) -> Unit) {
    val currentWidth  = width
    val currentHeight = height

    if (currentWidth > 0 && currentHeight > 0)
        callback(currentWidth, currentHeight)

    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        var w = 0
        var h = 0
        override fun onPreDraw(): Boolean {
            if (width != w || height != h) {
                w = width
                h = height
                callback(w, h)
            }
            viewTreeObserver.removeOnPreDrawListener(this)
            return true
        }
    })
}

fun View.message(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
fun View.banner(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).show()

inline fun View.onClickPopup(@MenuRes menu: Int, crossinline actions: (Int) -> Unit) = onClick {
    with(PopupMenu(context, this)) {
        menuInflater.inflate(menu, this.menu)
        setOnMenuItemClickListener { actions(it.itemId); true }
        show()
    }
}