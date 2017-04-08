@file:Suppress("UNCHECKED_CAST")

package com.chmelar.jozef.bcfiredroid.Screens.Project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.chmelar.jozef.bcfiredroid.API.Model.Project
import com.chmelar.jozef.bcfiredroid.API.Model.User
import com.chmelar.jozef.bcfiredroid.App
import com.chmelar.jozef.bcfiredroid.R
import com.chmelar.jozef.bcfiredroid.Screens.Project.AddPeople.AddToProjectPeopleActivity
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import extensions.android.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import kotlinx.android.synthetic.main.activity_project.*
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
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.email
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.onLongClick
import org.jetbrains.anko.sendSMS
import recycler.RecyclerListView
import recycler.hideFabOnScroll
import recycler.setDefaultDecorator
import recycler.showAs
import rx.whenAttached
import java.text.SimpleDateFormat
import java.util.*


class ProjectActivity : ToolbarActivity({
    contentView(R.layout.activity_project, View::login)


}, Navigation.up) {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {

            123 ->
                if ((grantResults.size > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //onCall()
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }

            else -> {}

        }
    }
}


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

private fun View.trips() = tripsRoot.whenAttached {
    activity.pager.onPageChangeListener { position, positionOffset ->
        if (position in 0..2 && positionOffset > 0.01) {
            fbAddTrip.hide()
        } else {
            fbAddTrip.show()
        }
    }
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
        val i = Intent(context, AddProjectActivity::class.java)
        i.putExtra("USERS_TRIP", project.employees)
        i.putExtra("PROJECT_TRIP", project)
        context.startActivity(i)
        activity.finish()
    }

    recyclerTrips.setDefaultDecorator(context)
    recyclerTrips.hideFabOnScroll(fbAddTrip)

}

private fun View.projectPeople() = peopleRoot.whenAttached {
    val project = activity.intent.getSerializableExtra("project") as Project
    //handle hiding fabs on viewpager
    activity.pager.onPageChangeListener { position, positionOffset ->
        if (position in 0..2 && positionOffset > 0.01) {
            fabMeeting.hide()
            fab_add.hide()
        } else {
            fabMeeting.show()
            fab_add.postDelayed({ fab_add.show() }, 100)
        }
    }

    recyclerPeople.setDefaultDecorator(context)
    recyclerPeople.show(project.employees.map(::employeeRow))
    //send email to all employees in project with datetime picker.
    fabMeeting.onClick {
        SingleDateAndTimePickerDialog.Builder(context)
            .title("Meeting time")
            .listener {
                val format = SimpleDateFormat("dd.MMM  HH:mm").format(it)

                val emailLauncher = Intent(Intent.ACTION_SEND_MULTIPLE)
                emailLauncher.type = "message/rfc822"
                emailLauncher.putExtra(Intent.EXTRA_EMAIL, project.employees.map { it.email }.toTypedArray())
                emailLauncher.putExtra(Intent.EXTRA_SUBJECT, "[${project.name}] ")
                emailLauncher.putExtra(Intent.EXTRA_TEXT, "Meeting @ $format .\n")
                context.startActivity(emailLauncher)
            }
            .display()
    }

    fab_add.onClick {
        val i = Intent(context, AddToProjectPeopleActivity::class.java)
        i.putExtra("currentUsers", project.employees)
        i.putExtra("currentId", project._id)
        context.startActivity(i)
    }

}

fun View.projectComments() {
    val project = activity.intent.getSerializableExtra("project") as Project
    val user = activity.intent.getSerializableExtra("user") as User
    recyclerComments.itemAnimator = FadeInAnimator(OvershootInterpolator(1f))
    recyclerComments.itemAnimator.addDuration=500
    getComments((activity.application as App), project, recyclerComments)

    btnAddComment.onClick {
        if (etCommentText.text.isNotEmpty()) {
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
    }

    etCommentText.onClick {
        recyclerComments.toBottom()
    }
}

fun getComments(app: App, project: Project, recycler: RecyclerListView) =
    app.api.getComments(project._id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .take(1)
        .subscribe(
            {
                if (it.isNotEmpty()) {
                    recycler.show(it.mapIndexed(::commentRow))
                    recycler.smoothScrollToPosition(recycler.adapter.itemCount - 1)
                }
            }
        )


fun commentRow(index: Int, comment: Comment) = R.layout.comment.showAs {
    if (index % 2 == 0) llCommentBack.backgroundColor = color(R.color.comment)
    else llCommentBack.backgroundColor = color(R.color.white)
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(comment.createdAt)
    val formattedDate = SimpleDateFormat("HH:mm dd.MMM").format(date).toString()
    tvCommentText.text = comment.text
    tvAuthor.text = comment.author.firstName + " " + comment.author.lastName
    tvDate.text = formattedDate
}

fun employeeRow(user: User) = R.layout.employee.showAs {
    if (user.phone.isNullOrEmpty()) ivPhone.beInvisible()

    tvMeno.text = "$user"
    tvPozicia.text = user.position

    ivEmail.onClick { context.email(user.email) }

    ivPhone.onLongClick { context.sendSMS(user.phone) }

    ivPhone.onClick {
        val permissionCheck = activity.checkSelfPermission(Manifest.permission.CALL_PHONE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                123)
        } else
            context.makeCall(user.phone)
    }

}
fun onCall(){

}
fun tripRow(trip: Trip) = R.layout.trip_item.showAs {
    if (!trip.date.isNullOrEmpty()) {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(trip.date)
        val format = SimpleDateFormat("dd.MMM").format(date)
        tvTripDate.text = format.toString()
    }

    tvCar.text = trip.car
    tvReason.text = trip.reason

    onClick {
        val i = Intent(context, TripsActivity::class.java)
        i.putExtra("USERS", (trip.employees))
        val project = activity.intent.getSerializableExtra("project") as Project
        i.putExtra("PROJECT_ID", project)
        context.startActivity(i)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
class TripsActivity : ToolbarActivity({
    contentView(R.layout.project_people, View::tripActivity)
    title = getString(R.string.attendet)

}, Navigation.up)

fun View.tripActivity() = peopleRoot.whenAttached {
    @Suppress("UNCHECKED_CAST")
    fabMeeting.beGone()
    fab_add.beGone()
    toolbar.beVisible()
    toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
    toolbar?.setNavigationOnClickListener { activity.supportFinishAfterTransition() }
    val users = activity.intent.getSerializableExtra("USERS") as ArrayList<User>
    recyclerPeople.show(users.map(::employeeRow))
}