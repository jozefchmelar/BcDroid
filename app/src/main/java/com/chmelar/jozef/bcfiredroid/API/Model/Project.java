package com.chmelar.jozef.bcfiredroid.API.Model;

import java.io.Serializable;
import java.util.LinkedList;

import lombok.Data;

@Data
public class Project implements Serializable {
    private Integer _id;
    private String name;
    private String number;
    private String costumer;
    private LinkedList<String> comments;
    public LinkedList<User> employees;
}