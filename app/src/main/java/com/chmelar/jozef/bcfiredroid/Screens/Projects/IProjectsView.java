package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import com.chmelar.jozef.bcfiredroid.API.Model.Project;

public interface IProjectsView {
    void loadAnimationOn();
    void loadAnimationOff();
    void sortListView();
    void addProject(Project value);
    void toast(String s);

    void TEST(Object value);
}