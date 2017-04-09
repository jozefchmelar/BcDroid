package com.chmelar.jozef.bcfiredroid.Screens.Projects.CreateProject;

import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.API.Model.User;

import java.util.List;

public interface ICreateProjectView {
    void done(Project p);

    void toast(String s);

    void displayUsers(List<User> value);

}