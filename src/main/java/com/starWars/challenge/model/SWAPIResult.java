package com.starWars.challenge.model;

import java.util.List;

public class SWAPIResult {

    private int count;
    private int lastPage;
    private List<PlanetInput> results;

    public SWAPIResult(int count, int lastPage, List<PlanetInput> results) {
        this.count = count;
        this.lastPage = lastPage;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public int getLastPage() {
        return lastPage;
    }

    public List<PlanetInput> getResults() {
        return results;
    }
}
