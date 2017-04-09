package com.chmelar.jozef.bcfiredroid.API.Model;

import java.io.Serializable;
import java.util.LinkedList;

import lombok.Builder;
import lombok.Data;

@Data
public class Project implements Serializable {
    public String _id;
    public String name;
    public String costumer;
    public LinkedList<String> comments;
    public LinkedList<User> employees;
}