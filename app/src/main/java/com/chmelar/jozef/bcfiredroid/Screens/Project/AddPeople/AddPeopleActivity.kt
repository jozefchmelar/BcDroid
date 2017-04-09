package com.chmelar.jozef.bcfiredroid.Screens.Project.AddPeople

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.chmelar.jozef.bcfiredroid.API.IApiRoutes
import com.chmelar.jozef.bcfiredroid.API.Model.AddPeopleToProjectRequest
import com.chmelar.jozef.bcfiredroid.API.Model.User
import com.chmelar.jozef.bcfiredroid.App
import com.chmelar.jozef.bcfiredroid.R
import com.chmelar.jozef.bcfiredroid.Screens.Project.employeeRow
import extensions.android.onClick
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_people.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.onItemClick
import java.util.*

class AddToProjectPeopleActivity : AppCompatActivity(), IAddPeopleView {

    private var usersToAdd: ArrayList<User> = ArrayList()
    private lateinit var presenter: AddToProjectPeoplePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_people)
        presenter = AddToProjectPeoplePresenter(this, (applicationContext as App).api)
        val currentId: String = intent.getStringExtra("currentId")
        rv_addppl_UsersToAdd.show(usersToAdd.map(::employeeRow))
        presenter.getUsers()
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { supportFinishAfterTransition() }
        toolbar.title = getString(R.string.addpoeple)
        fabAddingPeopleDone.onClick {
            presenter.addPeopleToProject(usersToAdd, currentId)
        }

    }

    override fun done(value: Boolean) {
        finish()
    }

    override fun displayUsers(value: List<User>) {

        val adapter = ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, value!!.toTypedArray())
        actvNamePpl.setAdapter(adapter)
        actvNamePpl.onItemClick { adapterView, view, i, l ->
            val item = adapter.getItem(i)
            if (!usersToAdd.contains(item)) usersToAdd.add(item)
            rv_addppl_UsersToAdd.show(usersToAdd.map(::employeeRow))
            actvNamePpl.setText("")
        }
    }
}

interface IAddPeopleView {
    fun displayUsers(value: List<User>)
    fun done(value: Boolean)
}

class AddToProjectPeoplePresenter(private val view: IAddPeopleView, private val client: IApiRoutes) {

    fun getUsers() {
        client.users
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe { view.displayUsers(it) }
    }

    fun addPeopleToProject(users: List<User>, projectID: String) {

        client.addPeopleToProject(AddPeopleToProjectRequest(people = users.map(User::_id)), projectID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe{view.done(it)}
    }

}