package com.starWars.challenge.model;

import java.util.List;

public class SWAPIResult {

    private int count;
    private String next;
    private String previous;
    private List<SWPlanet> results;

    public SWAPIResult(int count, String next, String previous, List<SWPlanet> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }


    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<SWPlanet> getResults() {
        return results;
    }
}
