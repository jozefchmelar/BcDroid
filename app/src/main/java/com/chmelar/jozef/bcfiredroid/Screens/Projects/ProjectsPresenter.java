package com.chmelar.jozef.bcfiredroid.Screens.Projects;

import com.chmelar.jozef.bcfiredroid.API.IApiRoutes;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.API.Model.User;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectsPresenter {

    private IProjectsView view;
    private IApiRoutes apiRoutes;

    public ProjectsPresenter(IProjectsView view, IApiRoutes api) {
        this.view = view;
        this.apiRoutes = api;
    }

    public void getProjectData(LoginResponse LoginResponse) {
        apiRoutes
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
        apiRoutes
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
                        view.toast("Error getting projects");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}