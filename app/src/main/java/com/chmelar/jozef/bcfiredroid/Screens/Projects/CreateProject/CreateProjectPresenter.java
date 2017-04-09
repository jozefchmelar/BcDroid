package com.chmelar.jozef.bcfiredroid.Screens.Projects.CreateProject;

import com.chmelar.jozef.bcfiredroid.API.IApiRoutes;
import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.API.Model.ProjectRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.User;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateProjectPresenter {

    private ICreateProjectView view;

    private IApiRoutes client;

    public CreateProjectPresenter(ICreateProjectView view, IApiRoutes client) {
        this.view = view;
        this.client = client;
    }

    public void getUsers() {
        client.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Observer<List<User>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> value) {
                        view.displayUsers(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void postProject(ProjectRequest project) {
        client.postProject(project)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Observer<Project>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Project value) {
                        view.done(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.toast(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}