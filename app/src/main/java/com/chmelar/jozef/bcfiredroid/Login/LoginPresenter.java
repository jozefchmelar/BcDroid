package com.chmelar.jozef.bcfiredroid.Login;


import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.RetrofitHolder;
import com.chmelar.jozef.bcfiredroid.Util;

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
        if (Util.isEmailValid(email))
            RetrofitHolder.getClient().getBcDroidService().login(new LoginRequest(email, password))
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
                            Log.d(TAG, "onNext: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.displayNetworkingError();
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onComplete() {
                            view.toggleLoadingAnimation();

                        }
                    });
        else view.setEmailFormatError();
    }


}