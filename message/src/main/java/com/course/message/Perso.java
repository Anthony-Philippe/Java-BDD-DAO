package com.course.message;

public class Perso {
    private String name;
    private Houses house;

    public Perso(String name, Houses house) {
        this.name = name;
        this.house = house;
    }

    public Object getHouse() {
        return house;
    }
}