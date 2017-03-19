package extensions.android

import android.support.annotation.LayoutRes
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

fun setTabsLayout(pager: ViewPager, tabs: TabLayout, @LayoutRes layout: Int) =
    (0 until pager.adapter.count).map(tabs::getTabAt).forEachIndexed { i, tab ->
        tab!!.setCustomView(layout)
        tab.text = pager.adapter.getPageTitle(i)
    }
