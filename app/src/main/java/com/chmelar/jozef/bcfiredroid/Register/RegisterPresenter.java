package com.chmelar.jozef.bcfiredroid.Register;


import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.RegisterRequest;
import com.chmelar.jozef.bcfiredroid.API.RetrofitHolder;
import com.chmelar.jozef.bcfiredroid.Util;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter {

    private static final String TAG = "RegisterPresenter";
    IRegisterView view;

    public RegisterPresenter(IRegisterView view) {
        this.view = view;
    }

    public void register(String email, String password,String repeatedPassword) {
        if(passwordsAreEqual(password,repeatedPassword) && validateEmail(email))
        RetrofitHolder.getClient().getBcDroidService().register(new RegisterRequest(email, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .take(1)
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.toggleLoadingAnimation();
                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        Log.d(TAG, "onNext: "+value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.toggleLoadingAnimation();
                    }
                });
    }

    private boolean validateEmail(String email) {
        if (!Util.isEmailValid(email)) {
            view.setMailFormatError();
            return false;
        } else {
            return true;
        }

    }

    private boolean passwordsAreEqual(String password, String repeatedPassword) {
        return password.equals(repeatedPassword);
    }
}
