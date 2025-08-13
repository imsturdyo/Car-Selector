package com.takehome.carselection.models;

import java.math.BigDecimal;

public class Vehicle {
    private Integer year;
    private String make;
    private String model;
    private String trim;
    private String body;
    private Integer seats;
    private BigDecimal msrp;
    private String fuel;

    public Vehicle() {
    }

    public Vehicle(Integer year, String make, String model, String trim, String body, Integer seats, BigDecimal msrp, String fuel) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.body = body;
        this.seats = seats;
        this.msrp = msrp;
        this.fuel = fuel;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
}