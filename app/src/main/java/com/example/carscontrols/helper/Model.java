package com.example.carscontrols.helper;

public class Model {
    int flag;
    String name,subtitle;

    public Model(int flag, String name, String subtitle){
        this.flag = flag;
        this.name = name;
        this.subtitle = subtitle;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
