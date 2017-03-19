package extensions.android

import android.support.annotation.MenuRes
import android.support.v7.widget.Toolbar

inline fun Toolbar.menu(@MenuRes resource: Int, crossinline actions: (id: Int) -> Unit) {
    menu.clear()
    inflateMenu(resource)
    setOnMenuItemClickListener { actions(it.itemId); true }
}