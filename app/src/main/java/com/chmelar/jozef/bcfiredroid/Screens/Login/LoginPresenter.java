package com.chmelar.jozef.bcfiredroid.Screens.Login;

import com.chmelar.jozef.bcfiredroid.API.IApiRoutes;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.Util.Util;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter {
    private ILoginView view;
    private String TAG = "loginPresenter";
    private IApiRoutes client;

    public LoginPresenter(ILoginView view, IApiRoutes client) {
        this.view = view;
        this.client = client;
    }

    public void login(String email, String password) {
        if (Util.isEmailValid(email)) {
            client.login(new LoginRequest(email, password))
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
                            view.toggleLoadingAnimation();
                        }

                        @Override
                        public void onComplete() {
                            view.toggleLoadingAnimation();
                        }
                    });
        } else {
            view.setEmailFormatError();
        }
    }

}