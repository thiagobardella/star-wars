package com.starWars.challenge.model;

import java.util.List;

public class SWPlanet {

    private String name;
    private String climate;
    private String terrain;
    private List<String> films;

    public SWPlanet(String name, String climate, String terrain, List<String> films) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.films = films;
    }

    public String getName() {
        return name;
    }

    public String getClimate() {
        return climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public List<String> getFilms() {
        return films;
    }
}
