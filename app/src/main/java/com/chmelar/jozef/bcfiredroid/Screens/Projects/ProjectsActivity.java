package com.chmelar.jozef.bcfiredroid.Screens.Projects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.App;
import com.chmelar.jozef.bcfiredroid.R;
import com.chmelar.jozef.bcfiredroid.Screens.Project.ProjectActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inloop.simplerecycleradapter.ItemClickListener;
import eu.inloop.simplerecycleradapter.SettableViewHolder;
import eu.inloop.simplerecycleradapter.SimpleRecyclerAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import lombok.val;

public class ProjectsActivity extends AppCompatActivity implements IProjectsView, ItemClickListener<Project>,IProjectDetail {

    private static final String TAG = "ProjectActivity";
    @BindView(R.id.projectsListView)
    RecyclerView projectsListView;
    @BindView(R.id.projectsRefresh)
    SwipeRefreshLayout projectsRefresh;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    private ProjectsPresenter presenter;
    private SimpleRecyclerAdapter<Project> projectAdapter;
    private ProjectComparator projectComparator = new ProjectComparator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        ButterKnife.bind(this);
        presenter = new ProjectsPresenter(this, ((App) getApplicationContext()).getApi());
        final LoginResponse loginResponse = (LoginResponse) getIntent().getSerializableExtra("LoginResponse");
//        getSupportActionBar().setTitle(loginResponse.getFullName());

        initAdapter();
        projectsListView.setAdapter(projectAdapter);
        presenter.getProjectData(loginResponse);
        projectsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectAdapter.clear(true);
                projectAdapter.notifyItemChanged(0, projectAdapter.getItemCount() - 1);
                presenter.getProjectData(loginResponse);
            }
        });

    }


    private void initAdapter() {
        final Activity activity = this;
        final IProjectDetail projectDetail=this;
        projectAdapter = new SimpleRecyclerAdapter<>(this, new SimpleRecyclerAdapter.CreateViewHolder<Project>() {
            @NonNull
            @Override
            protected SettableViewHolder<Project> onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ProjectDetailViewHolder(activity, R.layout.project_card, parent,projectDetail);
            }
        });
        projectsListView.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(1f)));
        projectsListView.getItemAnimator().setAddDuration(500);
        projectsListView.setLayoutManager(new LinearLayoutManager(this));
        projectsListView.getLayoutManager().setAutoMeasureEnabled(true);
        projectsListView.setNestedScrollingEnabled(false);
        projectsListView.setHasFixedSize(false);
        projectsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    @Override
    public void loadAnimationOn() {
        projectsRefresh.setRefreshing(true);
    }

    @Override
    public void loadAnimationOff() {
        projectsRefresh.setRefreshing(false);

    }

    @Override
    public void sortListView() {
        projectAdapter.sortItems(projectComparator);
        projectAdapter.notifyItemMoved(0, projectAdapter.getItemCount() - 1);
    }

    @Override
    public void addProject(Project value) {
        // true for notify changed
        projectAdapter.addItem(value, true);
        projectAdapter.notifyItemInserted(projectAdapter.getItemCount() - 1);
    }


    @Override
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(@NonNull Project item, @NonNull SettableViewHolder<Project> viewHolder, @NonNull View view) {

    }

    @OnClick(R.id.fab)
    public void onClick() {
        toast("fab");
    }

    @Override
    public void onPeopleClick(Project p) {
        val intent = new Intent(getApplicationContext(), ProjectActivity.class);
        intent.putExtra("screen",1);
        intent.putExtra("project",p);
        val user =  (LoginResponse) getIntent().getSerializableExtra("LoginResponse");
        intent.putExtra("user",user.getUser());
        startActivity(intent);
    }

    @Override
    public void onMessageClick(Project p) {
        val intent = new Intent(getApplicationContext(), ProjectActivity.class);
        intent.putExtra("project",p);
        intent.putExtra("screen",0);
        val user =  (LoginResponse)getIntent().getSerializableExtra("LoginResponse");
        intent.putExtra("user",user.getUser());
        startActivity(intent);

    }
}
