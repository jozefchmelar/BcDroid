package extensions.android

import android.view.*
import android.widget.LinearLayout

val ViewGroup.children: List<View> get() = (0 until childCount).map(this::getChildAt)

@Suppress("UNCHECKED_CAST")
fun <V: View> ViewGroup.child(index: Int) = getChildAt(index) as V

fun ViewGroup.keepVisibleOnly(id: Int) = children.forEach { it.beVisibleIf(it.id == id) }
fun ViewGroup.hideAllBut(id: Int) = children.forEach { it.beInvisibleIf(it.id != id) }

fun ViewGroup.inflate(id: Int, parent: ViewGroup = this) = LayoutInflater.from(context).inflate(id, parent, false)
inline fun <reified A: View> ViewGroup.inflateView(id: Int, parent: ViewGroup = this) = LayoutInflater.from(context).inflate(id, parent, false) as A

fun ViewGroup.replaceContent(view: View) {
    removeAllViews()
    addView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
}

fun ViewGroup.replaceContent(views: List<View>) {
    removeAllViews()
    views.forEach { addView(it, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)) }
}