package com.chmelar.jozef.bcfiredroid.API.Model;


import java.io.Serializable;
import java.util.LinkedList;

import lombok.Data;

@Data
public class User implements Serializable {
    private int _id;
    private String email;
    private String lastName;
    private String firstName;
    //LinkedList cause List is not implementing Serializable
    private LinkedList<Integer> projects;

    public User() {
    }
}
