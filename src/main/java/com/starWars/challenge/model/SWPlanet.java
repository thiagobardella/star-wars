package com.starWars.challenge.model;

import java.util.List;

public class SWPlanet {

    private String name;
    private String climate;
    private String terrain;
    private int filmsCount;

    public SWPlanet(String name, String climate, String terrain, int filmsCount) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.filmsCount = filmsCount;
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

    public int getFilmsCount() {
        return filmsCount;
    }
}
