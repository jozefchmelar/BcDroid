package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import android.support.annotation.NonNull;

import com.chmelar.jozef.bcfiredroid.API.Model.Project;

import java.util.Comparator;

public class ProjectComparator implements Comparator<Project> {



    @Override
    public int compare(Project project, Project project2) {
        if (project.get_id() < project2.get_id()) return 1;
        else if (project.get_id() > project2.get_id()) return -1;
        else return 0;
    }
}
