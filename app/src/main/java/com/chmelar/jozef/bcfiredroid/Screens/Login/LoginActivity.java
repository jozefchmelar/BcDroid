package com.chmelar.jozef.bcfiredroid.Screens.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.App;
import com.chmelar.jozef.bcfiredroid.R;
import com.chmelar.jozef.bcfiredroid.Screens.Projects.ProjectsActivity;
import com.chmelar.jozef.bcfiredroid.Screens.Register.RegisterActivity;
import com.chmelar.jozef.bcfiredroid.Util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_login)
    CardView btnLogin;
    @BindView(R.id.llLoginButtons)
    LinearLayout llLoginButtons;

    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.login_root)
    FrameLayout loginRoot;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this, ((App) getApplicationContext()).getApi());
        etEmail.setText("hoku1sa@pokus.sk");
        etPassword.setText("test");
    }

    @Override
    public void toggleLoadingAnimation() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            llLoginButtons.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            llLoginButtons.setVisibility(View.VISIBLE);
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
        ((App) getApplicationContext()).addToken(loginResponse.getToken());
        Intent intent = new Intent(this, ProjectsActivity.class);
        intent.putExtra("LoginResponse", loginResponse);
        Log.d(TAG, "goToProjects: " + loginResponse.toString());
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.btn_login)
    void onLoginClicked() {
        loginPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
        Util.hideSoftKeyboard(this);
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