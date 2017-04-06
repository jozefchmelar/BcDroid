package com.chmelar.jozef.bcfiredroid.Screens.Project

import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.chmelar.jozef.bcfiredroid.API.Model.Project
import com.chmelar.jozef.bcfiredroid.API.Model.User
import com.chmelar.jozef.bcfiredroid.App
import com.chmelar.jozef.bcfiredroid.R
import common.DividerItemDecoration
import common.VERTICAL_LIST
import extensions.android.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_project.view.*
import kotlinx.android.synthetic.main.comment.view.*
import kotlinx.android.synthetic.main.employee.view.*
import kotlinx.android.synthetic.main.project_comments.view.*
import kotlinx.android.synthetic.main.project_people.view.*
import kotlinx.android.synthetic.main.project_trips.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.trip_item.view.*
import navigation.Navigation
import navigation.ToolbarActivity
import org.jetbrains.anko.email
import org.jetbrains.anko.makeCall
import recycler.RecyclerListView
import recycler.showAs
import rx.whenAttached
import java.text.SimpleDateFormat
import java.util.*

class ProjectActivity : ToolbarActivity({
    contentView(R.layout.activity_project, View::login)
}, Navigation.up)

/**
 * When view is attached, I setup viewpager.
 */
private fun View.login() = projectRoot.whenAttached {
    val project = activity.intent.getSerializableExtra("project") as Project
    toolbar.title = project.name
    pager.adapter = projectAdapter()
    pager.adapter = projectAdapter()
    pager.offscreenPageLimit = 2
    home_tabs.setupWithViewPager(pager)
    pager.setCurrentItem(activity.intent.getIntExtra("screen", 0), true)
}

private class projectAdapter : PagerAdapter() {
    private val titles = listOf("Stav", "Riešiteľia", "Cesty")
    override fun isViewFromObject(view: View?, `object`: Any?) = view == `object`
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) = container.removeView(`object` as View)
    override fun getCount() = titles.size
    override fun getPageTitle(position: Int) = titles[position]

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        when (position) {
            0 -> container.inflate(R.layout.project_comments).apply(View::projectComments)
            1 -> container.inflate(R.layout.project_people).apply(View::projectPeople)
            2 -> container.inflate(R.layout.project_trips).apply(View::trips)
            else -> throw IllegalStateException("HomePage index $position")
        }.apply(container::addView)

}

private fun View.trips() {
    val project = activity.intent.getSerializableExtra("project") as Project
    val user = activity.intent.getSerializableExtra("user") as User
    (activity.application as App).api
        .getTrips(project._id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .take(1)
        .subscribe({
            recyclerTrips.show(it.map(::tripRow))
        },
            {
                Log.d("BAGABAGA", it.toString())
            }
        )
    fbAddTrip.onClick {
        val i = Intent(context,AddProjectActivity::class.java)
        i.putExtra("USERS_TRIP",project.employees)
        context.startActivity(i)
    }

}

private fun View.projectPeople() {
    val project = activity.intent.getSerializableExtra("project") as Project
    recyclerPeople.addItemDecoration(DividerItemDecoration(context, VERTICAL_LIST))
    recyclerPeople.show(project.employees.map(::employeeRow))
}

fun View.projectComments() {
    val project = activity.intent.getSerializableExtra("project") as Project
    val user = activity.intent.getSerializableExtra("user") as User
    getComments((activity.application as App), project, recyclerComments)
    btnAddComment.onClick {
        if (etCommentText.text.isNotEmpty())
            (activity.application as App).api
                .submitComment(SubmitComment(etCommentText.text.toString(), user._id), project._id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(
                    {
                        getComments((activity.application as App), project, recyclerComments)
                        etCommentText.setText("")
                    },
                    {
                        toast("err posting comment")
                    }
                )

    }
    etCommentText.onClick {
        postDelayed({ recyclerComments.toBottom() }, 100)
    }
}

fun getComments(app: App, project: Project, recycler: RecyclerListView) =
    app.api.getComments(project._id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .take(1)
        .subscribe(
            {
                recycler.show(it.map(::commentRow))
                recycler.smoothScrollToPosition(recycler.adapter.itemCount - 1)
            }
        )

fun employeeRow(user: User) = R.layout.employee.showAs {
    tvMeno.text = "${user.firstName}  ${user.lastName}"
    tvPozicia.text = user.position
    ivEmail.onClick { context.email(user.email) }
    if (user.phone.isNullOrEmpty()) ivPhone.beInvisible()
    ivPhone.onClick { context.makeCall(user.email) }
}

fun commentRow(comment: Comment) = R.layout.comment.showAs {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(comment.createdAt)
    val format = SimpleDateFormat("HH:mm dd.MMM").format(date)
    tvCommentText.text = comment.text
    tvAuthor.text = comment.author.firstName + " " + comment.author.lastName
    tvDate.text = format.toString()
}

fun tripRow(trip: Trip) = R.layout.trip_item.showAs {
    tvTripDate.text = "datum"
    tvCar.text = trip.car
    tvReason.text = trip.reason
    arrowFrame.onClick {
        val i = Intent(context, TripsActivity::class.java)
        i.putExtra("USERS", (trip.employees))
        context.startActivity(i)
    }

}

class TripsActivity : ToolbarActivity({
    contentView(R.layout.project_people, View::tripActivity)
}, Navigation.up)

fun View.tripActivity() = peopleRoot.whenAttached {
    val users = activity.intent.getSerializableExtra("USERS") as ArrayList<User>
    recyclerPeople.show(users.map(::employeeRow))

}