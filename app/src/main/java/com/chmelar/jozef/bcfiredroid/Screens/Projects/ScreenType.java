package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import java.util.HashMap;
import java.util.Map;

public enum ScreenType
{
    COMMENTS(0),
    PEOPLE(1),
    TRIPS(2);

    private int id; // Could be other data type besides int
    private ScreenType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static Map<Integer, ScreenType> buildMap() {
        Map<Integer, ScreenType> map = new HashMap<Integer, ScreenType>();
        ScreenType[] values = ScreenType.values();
        for (ScreenType value : values) {
            map.put(value.getId(), value);
        }

        return map;
    }
}
