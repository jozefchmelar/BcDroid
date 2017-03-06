package com.chmelar.jozef.bcfiredroid.Login;


import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.RetrofitHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter {
    private ILoginView view;
    private String TAG = "loginPresenter";

    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    public void login(String email, String password) {
        if (validateEmail(email))
            RetrofitHolder.getClient().login(new LoginRequest(email, password))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(1)
                    .subscribe(new Observer<LoginResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            view.toggleLoadingAnimation();
                        }

                        @Override
                        public void onNext(LoginResponse value) {
                            view.toggleLoadingAnimation();
                            Log.d(TAG, "onNext: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        else view.setEmailFormatError();
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
