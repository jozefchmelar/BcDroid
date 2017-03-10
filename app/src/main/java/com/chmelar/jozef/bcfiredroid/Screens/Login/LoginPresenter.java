package com.chmelar.jozef.bcfiredroid.Screens.Login;

import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.RetrofitHolder;
import com.chmelar.jozef.bcfiredroid.Util;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class LoginPresenter {
    private ILoginView view;
    private String TAG = "loginPresenter";
    private OkHttpClient client;
    public LoginPresenter(ILoginView view, OkHttpClient client) {
        this.view = view;
        this.client = client;
    }

    public void login(String email, String password) {
        if (Util.isEmailValid(email))
            RetrofitHolder.getInstance().getBcDroidService(client).login(new LoginRequest(email, password))
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
                             view.goToProjects(value);
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