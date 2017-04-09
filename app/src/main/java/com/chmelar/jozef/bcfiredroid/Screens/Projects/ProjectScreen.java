package com.chmelar.jozef.bcfiredroid.Screens.Projects;

public enum ProjectScreen
{
    COMMENTS(0),
    PEOPLE(1),
    TRIPS(2);

    private int id;
    private ProjectScreen(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}