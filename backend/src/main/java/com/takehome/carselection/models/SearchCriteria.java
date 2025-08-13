package com.takehome.carselection.models;

public class SearchCriteria {
    private String make;
    private String body;
    private Integer minSeats;
    private Double maxPrice;
    private String sort;
    private String order;

    public SearchCriteria() {
    }

    public SearchCriteria(String make, String body, Integer minSeats, Double maxPrice, String sort, String order) {
        this.make = make;
        this.body = body;
        this.minSeats = minSeats;
        this.maxPrice = maxPrice;
        this.sort = sort;
        this.order = order;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getMinSeats() {
        return minSeats;
    }

    public void setMinSeats(Integer minSeats) {
        this.minSeats = minSeats;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
