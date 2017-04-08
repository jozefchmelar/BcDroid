package com.chmelar.jozef.bcfiredroid.API.Model;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedList;

import lombok.Data;

@Data
public class User implements Serializable {
    public int _id;
    public String email;
    public String lastName;
    public String firstName;
    public String position;
    public String phone;
    //LinkedList cause List is not implementing Serializable
    public LinkedList<Integer> projects;

    public  String phoneNumber;

    public User() {
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public String getFirstName(){
        return Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
    }
    public String getLastName(){
        return Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
    }
}
