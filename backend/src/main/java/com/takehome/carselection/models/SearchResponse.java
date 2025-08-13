package com.takehome.carselection.models;

import java.util.List;

public class SearchResponse {
    private long total;
    private int page;
    private int pageSize;
    private List<Vehicle> results;

    public SearchResponse(long total, int page, int pageSize, List<Vehicle> results) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.results = results;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Vehicle> getResults() {
        return results;
    }

    public void setResults(List<Vehicle> results) {
        this.results = results;
    }
}

