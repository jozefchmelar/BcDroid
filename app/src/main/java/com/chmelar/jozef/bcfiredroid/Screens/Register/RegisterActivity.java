package com.chmelar.jozef.bcfiredroid.Screens.Register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.chmelar.jozef.bcfiredroid.App;
import com.chmelar.jozef.bcfiredroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    @BindView(R.id.etRegisterEmail)
    EditText etRegisterEmail;
    @BindView(R.id.etRegisterPassword)
    EditText etRegisterPassword;
    @BindView(R.id.etRegisterConfirmPassword)
    EditText etRegisterConfirmPassword;
    @BindView(R.id.progressBarRegister)
    ProgressBar progressBarRegister;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        presenter = new RegisterPresenter(this,((App)getApplicationContext()).getApi());
    }

    @Override
    public void displayPasswordNotMatching() {
        etRegisterConfirmPassword.setError("Passwords are not matching");
    }

    @Override
    public void toggleLoadingAnimation() {
        if (progressBarRegister.getVisibility() == View.INVISIBLE) {
            btnRegister.setVisibility(View.INVISIBLE);
            progressBarRegister.setVisibility(View.VISIBLE);
        } else {
            btnRegister.setVisibility(View.VISIBLE);
            progressBarRegister.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setMailFormatError() {
        etRegisterEmail.setError("Email not valid");
    }

    @OnClick(R.id.btnRegister)
    public void onClick() {
        presenter.register(etRegisterEmail.getText().toString(), etRegisterPassword.getText().toString(), etRegisterConfirmPassword.getText().toString());
    }
}
