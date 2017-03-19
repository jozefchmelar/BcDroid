package com.chmelar.jozef.bcfiredroid.Screens.Project

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.chmelar.jozef.bcfiredroid.API.Model.Project
import com.chmelar.jozef.bcfiredroid.API.Model.User
import com.chmelar.jozef.bcfiredroid.R
import common.DividerItemDecoration
import common.VERTICAL_LIST
import extensions.android.*
import kotlinx.android.synthetic.main.activity_project.view.*
import kotlinx.android.synthetic.main.employee.view.*
import kotlinx.android.synthetic.main.project_people.view.*
import navigation.ToolbarActivity
import org.jetbrains.anko.email
import org.jetbrains.anko.makeCall
import recycler.showAs
import rx.whenAttached

class ProjectActivity : ToolbarActivity({
    contentView(R.layout.activity_project, View::login)
 })

private fun View.login() = projectRoot.whenAttached {
    pager.adapter = projectAdapter()
    pager.offscreenPageLimit = 2
    home_tabs.setupWithViewPager(pager)
    setTabsLayout(pager, home_tabs, R.layout.tab_text_home)
}

private class projectAdapter : PagerAdapter() {
    private val titles = listOf("Comments", "People")

    override fun isViewFromObject(view: View?, `object`: Any?) = view == `object`
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) = container.removeView(`object` as View)
    override fun getCount() = titles.size
    override fun getPageTitle(position: Int) = titles[position]

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        when (position) {
            0 -> container.inflate(R.layout.project_comments).apply(View::projectComments)
            1 -> container.inflate(R.layout.project_people).apply(View::projectPeople)
            else -> throw IllegalStateException("HomePage index $position")
        }.apply(container::addView)

}

private fun View.projectPeople() {
    val project = activity.intent.getSerializableExtra("project") as Project
    recyclerPeople.addItemDecoration(DividerItemDecoration(context,VERTICAL_LIST))
    recyclerPeople.show(project.employees.map(::employeeRow))
}

private fun View.projectComments() {
    val project = activity.intent.getSerializableExtra("project")
}

fun employeeRow(user: User)= R.layout.employee.showAs {
    tvMeno.text="${user.firstName}  ${user.lastName}"
    tvPozicia.text="pozicia"
    ivEmail.onClick { context.email(user.email)}
    if(user.phoneNumber.isNullOrEmpty()) ivPhone.beGone()
    ivPhone.onClick { context.makeCall(user.email)}
}