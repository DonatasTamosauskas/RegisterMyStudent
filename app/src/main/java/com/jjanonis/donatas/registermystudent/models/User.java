package com.jjanonis.donatas.registermystudent.models;

public class User {
    private String UID;
    private String name;
    private String group;

    public User(){}

    public User(String UID, String name){
        this.UID = UID;
        this.name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
