package com.starWars.challenge.model;

public class PlanetInput {

    private String name;
    private String climate;
    private String terrain;
    private int filmsCount;

    public PlanetInput(String name, String climate, String terrain, int filmsCount) {
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
