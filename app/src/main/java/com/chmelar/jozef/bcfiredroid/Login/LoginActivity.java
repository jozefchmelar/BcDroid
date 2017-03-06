package com.chmelar.jozef.bcfiredroid.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chmelar.jozef.bcfiredroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginView {

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
        loginPresenter = new LoginPresenter(this);
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
    public void goToMainScreen() {

    }

    @Override
    public void registerScreen() {

    }

    @OnClick(R.id.btnLogin)
    void onLoginClicked() {
        loginPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
    }
    @OnClick(R.id.etEmail)
    void cleanField(){
    }
}

