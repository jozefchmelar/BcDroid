package com.chmelar.jozef.bcfiredroid.Screens.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.App;
import com.chmelar.jozef.bcfiredroid.BuildConfig;
import com.chmelar.jozef.bcfiredroid.R;
import com.chmelar.jozef.bcfiredroid.Screens.Projects.ProjectsActivity;
import com.chmelar.jozef.bcfiredroid.Screens.Register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this, ((App) getApplicationContext()).getClient());
        if (BuildConfig.DEBUG) {
            loginPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
        }

    }

    @Override
    public void toggleLoadingAnimation() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            btnLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setEmailFormatError() {
        etEmail.setError("invalid mail");
    }

    @Override
    public void displayNetworkingError() {
        Toast.makeText(this, "Error while getting data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToProjects(LoginResponse loginResponse) {
        Log.d(TAG, "goToProjects: "+loginResponse);
        ((App) getApplicationContext()).addToken(loginResponse.getToken());
        Intent intent = new Intent(this, ProjectsActivity.class);
        intent.putExtra("LoginResponse", loginResponse);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.btnLogin)
    void onLoginClicked() {
        loginPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @OnClick(R.id.etEmail)
    void cleanField() {
        etEmail.setText("");
    }

    @OnClick(R.id.tvRegister)
    public void onClick() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}