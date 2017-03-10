package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.User;
import com.chmelar.jozef.bcfiredroid.API.RetrofitHolder;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class ProjectsPresenter {

    private IProjectsView view;
    private OkHttpClient client;
private String TAG ="prjek";
    public ProjectsPresenter(IProjectsView view, OkHttpClient client) {
        this.view = view;
        this.client = client;
        Log.d(TAG, "ProjectsPresenter: " + client.toString());
    }

    public void getProjectData(LoginResponse LoginResponse) {
        RetrofitHolder
                .getInstance( )
                .getBcDroidService(client)
                .getUser(LoginResponse.getUser().get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(User value) {
                            view.setText(value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setText(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
