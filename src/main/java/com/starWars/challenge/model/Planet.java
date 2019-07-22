package com.starWars.challenge.model;

import javax.persistence.*;

@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String climate;
    private String terrain;

    @Column(name="films_count")
    private int filmsCount;

    public Planet() {
    }

    public Planet(int id, String name, String climate, String terrain, int filmsCount) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.filmsCount = filmsCount;
    }

    public Planet(PlanetInput planetInput) {
        this.name = planetInput.getName();
        this.climate = planetInput.getClimate();
        this.terrain = planetInput.getTerrain();
        this.filmsCount = planetInput.getFilmsCount();
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

    public int getFilmsCount() {
        return filmsCount;
    }


    @Override
    public boolean equals(Object obj) {
        Planet planet = (Planet)obj;
        return  this.name.equals(planet.name) &&
                this.climate.equals(planet.climate) &&
                this.terrain.equals(planet.terrain) &&
                this.filmsCount == planet.filmsCount;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.climate.hashCode() + this.terrain.hashCode() + this.filmsCount;
    }
}
