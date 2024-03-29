package com.starWars.challenge.repository;

import com.starWars.challenge.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Integer> {

    Planet findById(int id);
    Planet findByName(String name);

}
