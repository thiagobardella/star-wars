package com.starWars.challenge.model;

import java.util.List;

public class Planet {

    private int id;
    private String name;
    private String climate;
    private String terrain;
    private List<String> films;

    public Planet(int id, String name, String climate, String terrain, List<String> films) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.films = films;
    }

    public int getId() {
        return id;
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
//
//    public int getFilmsCount() {
//        return films.size();
//    }


    @Override
    public boolean equals(Object obj) {
        Planet planet = (Planet)obj;
        return  this.name.equals(planet.name) &&
                this.climate.equals(planet.climate) &&
                this.terrain.equals(planet.terrain) &&
                this.films.equals(planet.films);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.climate.hashCode() + this.terrain.hashCode() + this.films.hashCode();
    }
}
