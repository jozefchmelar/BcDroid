package com.chmelar.jozef.bcfiredroid.Screens.Projects;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.App;
import com.chmelar.jozef.bcfiredroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsActivity extends AppCompatActivity implements IProjectsView {

    private static final String TAG = "ProjectActivity";
    @BindView(R.id.textView)
    TextView textView;

    ProjectsPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        ButterKnife.bind(this);
        presenter= new ProjectsPresenter(this,((App)getApplicationContext()).getClient());
        LoginResponse loginResponse = (LoginResponse) getIntent().getSerializableExtra("LoginResponse");
        Log.d(TAG, "onCreate: "+loginResponse.toString());
        presenter.getProjectData(loginResponse);
    }

    @Override
    public void setText(String s) {
        textView.setText(s);
    }
}
