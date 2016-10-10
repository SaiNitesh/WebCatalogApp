package com.example.filipzoricic.online_shop;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by filipzoricic on 10/6/16.
 */
public class Container implements Serializable {

    private static final long serialVersionUID = 4466821913603037341L;
    private Hashtable<String, Integer> list;

    public Container(Hashtable<String, Integer> list) {
        this.list = list;
    }

    public Hashtable<String, Integer> getList() {
        return list;
    }

    public void setList(Hashtable<String, Integer> list) {
        this.list = list;
    }
}