package com.takehome.carselection.models;

import java.util.List;

public class Meta {
    private List<String> makes;
    private List<String> bodies;
    private List<Integer> seatOptions;

    public Meta(List<String> makes, List<String> bodies, List<Integer> seatOptions) {
        this.makes = makes;
        this.bodies = bodies;
        this.seatOptions = seatOptions;
    }

    public List<String> getMakes() {
        return makes;
    }

    public void setMakes(List<String> makes) {
        this.makes = makes;
    }

    public List<String> getBodies() {
        return bodies;
    }

    public void setBodies(List<String> bodies) {
        this.bodies = bodies;
    }

    public List<Integer> getSeatOptions() {
        return seatOptions;
    }

    public void setSeatOptions(List<Integer> seatOptions) {
        this.seatOptions = seatOptions;
    }
}