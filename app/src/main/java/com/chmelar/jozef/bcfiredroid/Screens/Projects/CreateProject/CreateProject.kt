package com.chmelar.jozef.bcfiredroid.Screens.Projects.CreateProject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.chmelar.jozef.bcfiredroid.API.Model.Project
import com.chmelar.jozef.bcfiredroid.API.Model.ProjectRequest
import com.chmelar.jozef.bcfiredroid.API.Model.User
import com.chmelar.jozef.bcfiredroid.App
import com.chmelar.jozef.bcfiredroid.R
import com.chmelar.jozef.bcfiredroid.Screens.Project.employeeRow
import kotlinx.android.synthetic.main.activity_create_project.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onItemClick
import java.util.*


class CreateProject : AppCompatActivity(), ICreateProjectView {

    private val usersToAdd = ArrayList<User>()
    private lateinit var presenter: CreateProjectPresenter
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { supportFinishAfterTransition() }
        toolbar.title = getString(R.string.newproject)
        presenter = CreateProjectPresenter(this, (applicationContext as App).api)
        user = intent.getSerializableExtra("user") as User
        usersToAdd.add(user)
        rvUsersToAdd.show(usersToAdd.map(::employeeRow))
        presenter.getUsers()

        fabCreateProjectDone.onClick {
            if (etName.text.isNotEmpty() && etCostumer.text.isNotEmpty()) {
                presenter.postProject(
                    ProjectRequest(
                    _id         = etId.text.toString(),
                    name        = etName.text.toString(),
                    costumer    = etCostumer.text.toString(),
                    employees   = usersToAdd.map(User::_id))
                )
            } else {
                etName.error        = "empty"
                etCostumer.error    = "empty"
            }
        }
    }

    override fun done(p: Project) {
        finish()
    }

    override fun toast(s: String) {
        toast(s)
    }

    override fun displayUsers(value: MutableList<User>?) {
        val adapter = ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, value!!.toTypedArray())
        actvUserNames.setAdapter(adapter)

        actvUserNames.onItemClick { adapterView, view, i, l ->
            val item = adapter.getItem(i)
            if (!usersToAdd.contains(item)) usersToAdd.add(item)
            rvUsersToAdd.show(usersToAdd.map(::employeeRow))
            actvUserNames.setText("")
            rvUsersToAdd.toBottom()
        }
    }

}