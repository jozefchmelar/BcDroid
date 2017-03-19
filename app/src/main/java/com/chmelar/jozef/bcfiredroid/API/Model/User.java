package com.chmelar.jozef.bcfiredroid.API.Model;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedList;

import lombok.Data;

@Data
public class User implements Serializable {
    private int _id;
    public String email;
    public String lastName;
    public String firstName;
    //LinkedList cause List is not implementing Serializable
    public LinkedList<Integer> projects;

    public  String phoneNumber;

    public User() {
    }
}
