package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.API.Model.User;
import com.chmelar.jozef.bcfiredroid.API.RetrofitHolder;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class ProjectsPresenter {

    private IProjectsView view;
    private OkHttpClient client;
    private String TAG = this.getClass().getName().toUpperCase();

    public ProjectsPresenter(IProjectsView view, OkHttpClient client) {
        this.view = view;
        this.client = client;
    }

    public void getProjectData(LoginResponse LoginResponse) {
        RetrofitHolder
                .getInstance()
                .getBcDroidService(client)
                .getUser(LoginResponse.getUser().get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.loadAnimationOn();

                    }

                    @Override
                    public void onNext(User value) {
                        for (Integer projectNumber : value.getProjects()) {
                            getUserProject(projectNumber);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.toast(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        view.loadAnimationOff();
                        view.sortListView();
                    }
                });
    }

    private void getUserProject(int id) {
        final List<Project> projects = new LinkedList<>();
        RetrofitHolder
                .getInstance()
                .getBcDroidService(client)
                .getProject(id)
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Project>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Project value) {
                        view.addProject(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                        view.toast("Error gettin projects");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
