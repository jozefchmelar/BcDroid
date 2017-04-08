package com.chmelar.jozef.bcfiredroid.Screens.Project

import android.app.DatePickerDialog
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.chmelar.jozef.bcfiredroid.API.Model.Project
import com.chmelar.jozef.bcfiredroid.API.Model.User
import com.chmelar.jozef.bcfiredroid.App
import com.chmelar.jozef.bcfiredroid.R
import extensions.android.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_project.view.*
import navigation.Navigation
import navigation.ToolbarActivity
import org.jetbrains.anko.onItemClick
import recycler.RecyclerListView
import recycler.hideFabOnScroll
import rx.whenAttached
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class AddProjectActivity : ToolbarActivity({
    contentView(R.layout.activity_add_project, View::addProject)
}, Navigation.up)

fun View.addProject() = addTripRoot.whenAttached {
    @Suppress("UNCHECKED_CAST")
    val users = activity.intent.getSerializableExtra("USERS_TRIP") as ArrayList<User>
    val project = activity.intent.getSerializableExtra("PROJECT_TRIP") as Project

    rvUsersToAdd.hideFabOnScroll(fabTripDone)
    val adapter = ArrayAdapter<User>(context, android.R.layout.simple_list_item_1, users.toTypedArray())
    val usersToAdd = ArrayList<User>()
    actvName.setAdapter(adapter)
    actvName.onItemClick { adapterView, view, i, l ->
        val item = adapter.getItem(i)
        if (!usersToAdd.contains(item)) usersToAdd.add(item)
        rvUsersToAdd.show(usersToAdd.map(::employeeRow))
        actvName.setText("")
    }


    val myCalendar = Calendar.getInstance()
    val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etDateTime.setText(sdf.format(myCalendar.time))
    }
    etDateTime.onClick {
        DatePickerDialog(context, date, myCalendar
            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    fabTripDone.onClick {
        if (etReason.text.isEmpty() ||
            etUsedCar.text.isEmpty() ||
            etDateTime.text.isEmpty() ||
            usersToAdd.isEmpty()) {
            message("All fields must be filled")
        } else {
            (activity.application as App).api
                .postTrip(TripRequest(
                    car = etUsedCar.text.toString(),
                    reason = etReason.text.toString(),
                    date = etDateTime.text.toString(),
                    employees = usersToAdd.map(User::_id)),project._id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(
                    {
                       Log.d("BAGA",it.toString())
                        val i = Intent(context,ProjectActivity::class.java)
                        toast("ok")
                        activity.finish()
                    },
                    {
                        toast("err posting comment")
                    }
                )
        }
    }

}




data class TripRequest(
    val car: String,
    val reason: String,
    val date: String,
    val employees:List<Int>
)
data class TripResponse (
    val trip: TripRequest,
    val success:Boolean
)