package extensions.android

import android.support.annotation.LayoutRes
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

fun setTabsLayout(pager: ViewPager, tabs: TabLayout, @LayoutRes layout: Int) =
    (0 until pager.adapter.count).map(tabs::getTabAt).forEachIndexed { i, tab ->
        tab!!.setCustomView(layout)
        tab.text = pager.adapter.getPageTitle(i)
    }


fun ViewPager.onPageChangeListener(action: (Int, Float) -> Unit) =
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) { }
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { action(position, positionOffset) }
        override fun onPageSelected(position: Int) { }
    })