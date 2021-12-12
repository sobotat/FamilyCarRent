package cz.familycarrent.api;

import java.util.Date;

public class Vehicle {

    String name;
    String owner;/*
    Date date;
    String why;
    String status;

    public Vehicle(String name, String owner, Date date, String why, String status) {
        this.name = name;
        this.owner = owner;
        this.date = date;
        this.why = why;
        this.status = status;
    }*/

    public Vehicle(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }
}
