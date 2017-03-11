package com.chmelar.jozef.bcfiredroid.API.Model;

import java.util.LinkedList;

import lombok.Data;

@Data
public class Project {
    private Integer _id;
    private String name;
    private String number;
    private String costumer;
    private LinkedList<String> comments;
    private LinkedList<User> employees;
}